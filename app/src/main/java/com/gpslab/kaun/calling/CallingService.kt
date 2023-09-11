package com.gpslab.kaun.calling

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gpslab.kaun.R
import com.gpslab.kaun.chat.CallsManager
import com.gpslab.kaun.retrofit.Log
import com.gpslab.kaun.view.*
import io.agora.rtc.Constants
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoEncoderConfiguration
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

class CallingService : Service(), ProximitySensor.Delegate, AudioManager.OnAudioFocusChangeListener, DuringCallEventHandler {
    private var callingState: CallingState = CallingState.NONE
    private var isCallActivityVisible = false
    private var isSpeakerEnabled = false
    private var isMicMuted = false
    private var isLocalVideoEnabled = false
    private var isCallActive = false
    private var isIncoming = false
    private val callingServiceInterface = CallingServiceInterface()

    var proximitySensor: ProximitySensor? = null
    var audioManager: AudioManager? = null
    var ringtonePlayer: RingtonePlayer? = null
    private var notificationId = -1
    private var hasAnswered = false

    private var timeObservable: Disposable? = null
    private var callTimeoutDeferDisposable: Disposable? = null

    var fireManager = FireManager()
    var channelName = ""
    private var callDuration = 0L


    private var fireCall: FireCall? = null

    private var videoUids = hashMapOf<Int, Boolean>()
    private val usersUids = hashMapOf<Int, Boolean>()
    private var isVideoCall = false
    private val callTimeoutDuration = 30L
    private val disposables = CompositeDisposable()
    private val callManager = CallsManager()
    private var audioFocusRequest: AudioFocusRequest? = null

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        proximitySensor = ProximitySensor(this, this)
        ringtonePlayer = RingtonePlayer(this)
        Log.d("UpdateUIUpdateCalling", "Service    onCreate    ====     ")
    }

    companion object {
        fun getStartIntent(context: Context, fireCall: FireCall, actionType: Int): Intent {
            Log.d("UpdateUIUpdateCalling", "Service    getStartIntent    ====     ")
            return Intent(context, CallingService::class.java).apply {
                putExtra(IntentUtils.CALL_TYPE, CallType.fromInt(fireCall.callType))
                putExtra(IntentUtils.CALL_DIRECTION, fireCall.direction)
                putExtra(IntentUtils.CALL_ID, fireCall.callId)
                putExtra(IntentUtils.UID, fireCall.user.uid)
                putExtra(IntentUtils.PHONE, fireCall.phoneNumber)
                putExtra(IntentUtils.ISVIDEO, fireCall.isVideo)
                putExtra(IntentUtils.CALL_ACTION_TYPE, actionType)
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.d("UpdateUIUpdateCalling", "Service    onStartCommand    ====     ")
        if (intent != null) {
            if (intent.hasExtra(IntentUtils.CALL_ACTION_TYPE)) {
                val action = intent.getIntExtra(IntentUtils.CALL_ACTION_TYPE, -1)
                val callId = intent.getStringExtra(IntentUtils.CALL_ID)

                when (action) {
                    IntentUtils.NOTIFICATION_ACTION_START_INCOMING -> {

                        RealmHelper.getInstance().getFireCall(callId)?.let { fireCall ->

                            //prevent multiple incoming calls
                            if (MyApp.isIsCallActive()) return@let

                            MyApp.setCallActive(true)


                            this@CallingService.fireCall = fireCall
                            isIncoming = true
                            val uid = fireCall.user.uid
                            val callId = fireCall.callId
                            Intent(this@CallingService, CallingActivity::class.java).apply {
                                putExtra(IntentUtils.CALL_DIRECTION, FireCallDirection.INCOMING)
                                putExtra(IntentUtils.CALL_TYPE, fireCall.callType)
                                putExtra(IntentUtils.CALL_ID, callId)
                                putExtra(IntentUtils.UID, uid)
                                putExtra(IntentUtils.PHONE, fireCall.phoneNumber)
                                putExtra(IntentUtils.CALL_ACTION_TYPE, IntentUtils.NOTIFICATION_ACTION_START_INCOMING)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                this@CallingService.startActivity(this)
                            }

                            val incomingCallNotification = NotificationHelper(this@CallingService).createIncomingCallNotification(fireCall, getNotificationId())
                            startForeground(getNotificationId(), incomingCallNotification)
                            if (ringtonePlayer != null && audioManager!!.ringerMode == AudioManager.RINGER_MODE_NORMAL) {
                                requestAudioFocus(AudioManager.STREAM_VOICE_CALL)
                                ringtonePlayer!!.playIncomingRingtone()
                            }

                            startDefer()

                            if (!fireCall.isGroupCall) {

//                                callManager.listenForEndingCall(fireCall.callId, fireCall.user.uid, isIncoming).subscribe({
//                                    if (it.exists()) {
//                                        endCall(CallEndedReason.REMOTE_REJECTED)
//                                    }
//                                }, { error ->
//
//                                }).addTo(disposables)
                            }
                        }

                    }

                    IntentUtils.NOTIFICATION_ACTION_DECLINE -> {
                        rejectCall()
                    }
                }
            }
        }
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        Log.d("UpdateUIUpdateCalling", "Service    onDestroy    ====     ")
        stop()
        super.onDestroy()
    }

    private fun setCallEnded(callId: String, otherUid: String, isIncoming: Boolean) {
        Log.d("UpdateUIUpdateCalling", "Service    setCallEnded    ====     ")
        fireCall?.let { fireCall ->
            if (!fireCall.isGroupCall) {
//                ServiceHelper.setCallEnded(this, callId, otherUid, isIncoming)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun stop() {

        Log.d("UpdateUIUpdateCalling", "Service    stop    ====     ")
        videoUids.clear()
        usersUids.clear()
        setCallingState(CallingState.NONE)
        isLocalVideoEnabled = false


        leaveChannel()

        setCallActive(false)
        ringtonePlayer?.stopRingtone()

        setBluetoothHeadset(false)
        audioManager?.abandonAudioFocusCompat(this, audioFocusRequest)

        stopListenForSensor()

        fireCall?.let { call ->

            RealmHelper.getInstance().updateCallInfoOnCallEnded(call.callId, callDuration.toInt())

        }

        NotificationHelper(this@CallingService).cancelIncomingCallNotification()
        notificationId = -1

        application().removeEventHandler(this@CallingService)
        timeObservable?.dispose()
        callTimeoutDeferDisposable?.dispose()
        timeObservable = null
        callTimeoutDeferDisposable = null
        callDuration = 0

        disposables.dispose()


        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(IntentUtils.ACTION_FINISH_CALLING_ACTIVITY))
        stopForeground(true)

        stopSelf()


    }

    private fun setCallActive(mCallActive: Boolean) {
        Log.d("UpdateUIUpdateCalling", "Service    setCallActive    ====     ")
        isCallActive = mCallActive
        MyApp.setCallActive(mCallActive)
    }


    override fun onBind(intent: Intent): IBinder? {
        Log.d("UpdateUIUpdateCalling", "Service    onBind    ====     ")
        return callingServiceInterface
    }


    private fun requestAudioFocus(streamType: Int) {
        Log.d("UpdateUIUpdateCalling", "Service    requestAudioFocus    ====     ")
        audioFocusRequest = audioManager?.requestAudioFocusCompat(this, streamType, AudioManager.AUDIOFOCUS_GAIN)
    }

    private fun startForeground() {
        Log.d("UpdateUIUpdateCalling", "Service    startForeground    ====     ")
        setCallActive(true)
        val streamType = AudioManager.STREAM_VOICE_CALL

        fireCall?.let { fireCall ->
            val activeCallNotification = NotificationHelper(this).createActiveCallNotification(fireCall, getNotificationId())
            startForeground(getNotificationId(), activeCallNotification)
            RealmHelper.getInstance().saveObjectToRealm(fireCall)
            requestAudioFocus(streamType)
        }
    }


    private fun setBluetoothHeadset(enable: Boolean) {
        Log.d("UpdateUIUpdateCalling", "Service    setBluetoothHeadset    ====     ")
        if (enable) {
            audioManager?.mode = AudioManager.MODE_NORMAL
            audioManager?.isBluetoothScoOn = true
            audioManager?.startBluetoothSco()
            audioManager?.mode = AudioManager.MODE_IN_CALL
        } else {
            audioManager?.isBluetoothScoOn = false
            audioManager?.stopBluetoothSco()
            audioManager?.mode = AudioManager.MODE_NORMAL
        }
    }


    override fun onProximitySensorNear() {
        Log.d("UpdateUIUpdateCalling", "Service    onProximitySensorNear    ====     ")
        if (isCallActivityVisible) proximitySensor?.acquire()
    }

    override fun onProximitySensorFar() {
        Log.d("UpdateUIUpdateCalling", "Service    onProximitySensorFar    ====     ")
        proximitySensor?.release()
    }


    private fun setCallingState(callingState: CallingState) {
        Log.d("UpdateUIUpdateCalling", "Service    setCallingState    ====     ")
        this.callingState = callingState
        postViewEvent(CallingViewState.UpdateCallingState(callingState))
    }


    private fun onCallEstablished() {
        Log.d("UpdateUIUpdateCalling", "Service    onCallEstablished    ====     ")
        callTimeoutDeferDisposable?.dispose()
        postViewEvent(CallingViewState.OnCallEstablished)
        if (timeObservable == null) {
            timeObservable = Observable.interval(1, TimeUnit.SECONDS).subscribe({ interval ->
                postViewEvent(CallingViewState.UpdateDuration(interval))
            }, { error ->

            }).addTo(disposables)
        }

        ringtonePlayer?.stopRingtone()


        //divert sound to bluetooh headset if available
        if (AudioHelper.isBluetoothHeadsetOn(audioManager))
            setBluetoothHeadset(true)

        setCallActive(true)

        fireCall?.let { call ->
            if (call.direction == FireCallDirection.INCOMING) {
                if (call.isGroupCall) {
//                    callManager.setCallAnsweredForGroup(call.callId, call.user.uid)
                } else {
//                    callManager.setCallAnswered(call.callId, call.user.uid, true)
                }
                RealmHelper.getInstance().setCallAsAnswered(call.callId)
                val notificationHelper = NotificationHelper(this)
                val activeCallNotification = notificationHelper.createActiveCallNotification(call, getNotificationId())
                notificationHelper.notifyNotification(getNotificationId(), activeCallNotification)
            }
        }
    }


    override fun onExtraCallback(type: Int, data: Array<Any?>) {
        Log.d("UpdateUIUpdateCalling", "Service    onExtraCallback    ====    data  ===    ===   " + data[0].toString())
        Log.d("UpdateUIUpdateCalling", "Service    onExtraCallback    ====    muted  ===    ===   " + data[1].toString())
        Handler(Looper.getMainLooper()).post {

            when (type) {

                AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED -> {

                    val peerUid = data[0] as Int
                    val muted = data[1] as Boolean

                    postViewEvent(CallingViewState.MuteOrUnmuteRemoteViewForUid(peerUid, muted))
                }

                AGEventHandler.EVENT_TYPE_CONNECTION_STATE_CHANGED -> {
                    val state = data[0] as? Int
                    val reason = data[1] as? Int

                    state?.let { connectionState ->

                        when (connectionState) {

                            Constants.CONNECTION_STATE_CONNECTING -> {
                                setCallingState(CallingState.CONNECTING)
                            }
                            Constants.CONNECTION_STATE_CONNECTED -> {
                                setCallingState(CallingState.CONNECTED)
                            }
                            Constants.CONNECTION_STATE_FAILED -> {
                                setCallingState(CallingState.FAILED)
                            }

                            Constants.CONNECTION_STATE_RECONNECTING -> {
                                setCallingState(CallingState.RECONNECTING)
                            }

                        }
                    }

                }
            }
        }
    }


    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        Log.d("UpdateUIUpdateCalling", "Service    onJoinChannelSuccess    ====     ")
        Handler(Looper.getMainLooper()).post {

            postViewEvent(CallingViewState.JoinChannelSuccess(uid))
        }
    }

    override fun onUserJoined(uid: Int) {
        Log.d("UpdateUIUpdateCalling", "Service    onUserJoined    ====     ")
        Handler(Looper.getMainLooper()).post {
            hasAnswered = true
            //check if this user joins for the first time
            if (usersUids.isEmpty()) {
                setCallingState(CallingState.ANSWERED)
                onCallEstablished()
            }

            usersUids[uid] = true
        }

    }

    override fun onDecodingRemoteVideo(uid: Int, elapsed: Int) {
        Log.d("UpdateUIUpdateCalling", "Service    onDecodingRemoteVideo    ====     ")
        Handler(Looper.getMainLooper()).post {

            videoUids[uid] = false
            postViewEvent(CallingViewState.SetupRemoteViewForUid(uid))
        }
    }


    override fun onUserOffline(uid: Int, reason: Int) {
        Log.d("UpdateUIUpdateCalling", "Service    onUserOffline    ====     ")
        Handler(Looper.getMainLooper()).post {

            videoUids.remove(uid)
            usersUids.remove(uid)
            postViewEvent(CallingViewState.RemoveRemoteViewForUid(uid))
            if (videoUids.isEmpty() || areAllUsersMuted()) {
                postViewEvent(CallingViewState.HideRemoteViews)
            }

            if (usersUids.isEmpty()) {
                if (fireCall?.isGroupCall == false) {
                    endCall(CallEndedReason.REMOTE_HUNG_UP)
                }
            }
        }
    }

    private fun areAllUsersMuted(): Boolean = videoUids.values.all { it }


    fun endCall(reason: CallEndedReason) {
        Log.d("UpdateUIUpdateCalling", "Service    endCall    ====     ")
        postViewEvent(CallingViewState.CallEnded(reason))
        stop()
    }

    fun setSpeakerEnabled(isEnabled: Boolean) {
        Log.d("UpdateUIUpdateCalling", "Service    setSpeakerEnabled    ====     ")
        isSpeakerEnabled = isEnabled
        rtcEngine().setEnableSpeakerphone(isSpeakerEnabled)

        if (isEnabled) {
            stopListenForSensor()
        } else {
            startListenForSensor()
        }
    }

    override fun onAudioFocusChange(i: Int) {}

    private fun doConfigEngine() {
        Log.d("UpdateUIUpdateCalling", "Service    doConfigEngine    ====     ")
        val videoDimension = AgoraConfig.videoDimension
        val videoFps = AgoraConfig.videoFps
        configEngine(videoDimension, videoFps, null, null)
    }

    private fun joinChannel(channelName: String, uid: Int): Int {
        Log.d("UpdateUIUpdateCalling", "Service    joinChannel    ====     ")

        this@CallingService.channelName = channelName

        // 1. इसमें शामिल होने के बाद उपयोगकर्ता केवल एक दूसरे को देख सकते हैं
        // वही चैनल सफलतापूर्वक उसी ऐप आईडी का उपयोग कर रहा है।
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. एक टोकन केवल उस चैनल के नाम के लिए मान्य है जो
        // आप इस टोकन को जेनरेट करने के लिए उपयोग करते हैं।
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
        val token = getString(R.string.agora_access_token)
        Log.d("UpdateUIUpdateCalling", "Service  joinChannel  channelName    ====     " + channelName)
        Log.d("UpdateUIUpdateCalling", "Service   joinChannel   uid    ====     " + uid)
        val result = rtcEngine().joinChannel(token, "ChannelOne", "Extra Optional Data", uid)

        //SUCCESS
        if (result == 0) {
            config().mChannel = channelName
        }

        return result
    }


    private fun startDefer() {
        Log.d("UpdateUIUpdateCalling", "Service    startDefer    ====     ")
        if (callTimeoutDeferDisposable == null) {
            callTimeoutDeferDisposable = Completable.complete()
                    .delay(callTimeoutDuration, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        fireCall?.let { fireCall ->
                            setCallEnded(fireCall.callId, fireCall.user.uid, isIncoming)
                        }

                        endCall(CallEndedReason.NO_ANSWER)
                    }, { error ->

                    }).addTo(disposables)
        }


    }


    private fun leaveChannel() {
        Log.d("UpdateUIUpdateCalling", "Service    leaveChannel    ====     ")
        config().mChannel = null
        rtcEngine().leaveChannel()
        config().reset()
    }

    fun postViewEvent(viewEvent: CallingViewState) {
        Log.d("UpdateUIUpdateCalling", "Service    postViewEvent    ====     ")
        EventBus.getDefault().post(viewEvent)
    }

    private fun configEngine(videoDimension: VideoEncoderConfiguration.VideoDimensions?, fps: VideoEncoderConfiguration.FRAME_RATE?, encryptionKey: String?, encryptionMode: String?) {
        Log.d("UpdateUIUpdateCalling", "Service    configEngine    ====     ")

        // Set the Resolution, FPS. Bitrate and Orientation of the video
        rtcEngine().setVideoEncoderConfiguration(VideoEncoderConfiguration(videoDimension,
                fps,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT))
    }


    private fun stopListenForSensor() {
        Log.d("UpdateUIUpdateCalling", "Service    stopListenForSensor    ====     ")
        proximitySensor?.stopListenForSensor()
        proximitySensor?.release()
    }

    private fun application(): MyApp {
        Log.d("UpdateUIUpdateCalling", "Service    application    ====     ")
        return application as MyApp
    }

    private fun rtcEngine(): RtcEngine {
        Log.d("UpdateUIUpdateCalling", "Service    rtcEngine    ====     ")
        return application().rtcEngine()
    }

    private fun config(): EngineConfig {
        Log.d("UpdateUIUpdateCalling", "Service    config    ====     ")
        return application().config()
    }


    private fun startListenForSensor() {
        Log.d("UpdateUIUpdateCalling", "Service    startListenForSensor    ====     ")
        if (fireCall?.isVideo == true) return

        if (!AudioHelper.isHeadsetOn(audioManager) && !isSpeakerEnabled) {
            proximitySensor?.listenForSensor()
        }
    }

    private fun getNotificationId(): Int {
        Log.d("UpdateUIUpdateCalling", "Service    getNotificationId    ====     ")
        if (notificationId == -1) notificationId = NotificationHelper.generateId()
        return notificationId
    }


    fun startCall(fireCall: FireCall, isIncoming: Boolean) {
        Log.d("UpdateUIUpdateCalling", "Service    startCall    ====     ")
        MyApp.setCallActive(true)
        this@CallingService.fireCall = fireCall
        this@CallingService.isIncoming = isIncoming

        setCallingState(CallingState.INITIATING)
        application().addEventHandler(this@CallingService)

        this@CallingService.channelName = fireCall.channel
        if (fireCall.isVideo) {
            isLocalVideoEnabled = true
            isVideoCall = true
            doConfigEngine()
            rtcEngine().enableVideo()
            postViewEvent(CallingViewState.SetupLocalView)
        } else {
            rtcEngine().disableVideo()
        }


        isSpeakerEnabled = fireCall.isVideo

        startDefer()
        val joinChannelResult = joinChannel(channelName, config().mUid)

        startForeground()
        if (!isIncoming) {
            ringtonePlayer?.playProgressTone()
        }
        //if Success
        if (joinChannelResult == 0) {
            if (!isIncoming) {

                if (fireCall.isGroupCall) {
//                    callManager.saveOutgoingGroupCallOnFirebase(fireCall, fireCall.user.uid).subscribe({
//
//                    }, { error ->
//                        endCall(CallEndedReason.ERROR)
//                    }).addTo(disposables)

                } else {

//                    callManager.saveOutgoingCallOnFirebase(fireCall, fireCall.user.uid).subscribe({
//                    }, { error ->
//                        endCall(CallEndedReason.ERROR)
//                    }).addTo(disposables)
//
//
//                    callManager.listenForEndingCall(fireCall.callId, fireCall.user.uid, isIncoming).subscribe({
//                        if (it.exists()) {
//                            endCall(CallEndedReason.REMOTE_HUNG_UP)
//                        }
//                    }, { error ->
//
//                    }).addTo(disposables)
                }

            }


        } else {
            endCall(CallEndedReason.ERROR)
        }


    }


    private fun rejectCall() {
        Log.d("UpdateUIUpdateCalling", "Service    rejectCall    ====     ")
        fireCall?.let { fireCall ->
            postViewEvent(CallingViewState.CallEnded(CallEndedReason.LOCAL_REJECTED))


            if (fireCall.isGroupCall) {
//                ServiceHelper.setCallDeclinedForGroup(this, fireCall.callId, fireCall.user.uid)
            } else {
                setCallEnded(fireCall.callId, fireCall.user.uid, isIncoming)
            }
        }
        stop()
    }

    inner class CallingServiceInterface : Binder() {

        fun setStateEvent(stateEvent: CallingStateEvent) {
            Log.d("UpdateUIUpdateCalling", "Service    CallingServiceInterface    ====     ")
            when (stateEvent) {

                is CallingStateEvent.StartCall -> {
                    startCall(stateEvent.fireCall, stateEvent.isIncoming)
                }


                is CallingStateEvent.SpeakerClicked -> {
                    setSpeakerEnabled(!isSpeakerEnabled)
                    if (isSpeakerEnabled) {
                        postViewEvent(CallingViewState.EnableSpeaker)
                    } else {
                        postViewEvent(CallingViewState.DisableSpeaker)
                    }
                }

                is CallingStateEvent.BtnVideoClicked -> {
                    isLocalVideoEnabled = !isLocalVideoEnabled
                    rtcEngine().muteLocalVideoStream(!isLocalVideoEnabled)
                    if (isLocalVideoEnabled) {
                        postViewEvent(CallingViewState.ResumeLocalVideo)
                    } else {
                        postViewEvent(CallingViewState.PauseLocalVideo)
                    }

                }

                is CallingStateEvent.MicClicked -> {
                    isMicMuted = !isMicMuted
                    rtcEngine().muteLocalAudioStream(isMicMuted)
                    postViewEvent(CallingViewState.MicMuted(isMicMuted))
                }

                is CallingStateEvent.FlipCameraClicked -> {
                    rtcEngine().switchCamera()
                }

                is CallingStateEvent.EndCall -> {
                    fireCall?.let { fireCall ->
                        setCallEnded(fireCall.callId, fireCall.user.uid, isIncoming)
                    }
                    endCall(CallEndedReason.LOCAL_HUNG_UP)
                }

                is CallingStateEvent.OnStart -> {
                    MyApp.phoneCallActivityResumed()

                    if (isVideoCall) {
                        videoUids.keys.forEach { uid ->

                            //check if it's not muted
                            videoUids[uid]?.let { isMuted ->
                                if (!isMuted) {
                                    rtcEngine().muteRemoteVideoStream(uid, false)
                                }

                                //update non-synced remote views while on Stop
                                postViewEvent(CallingViewState.SetupRemoteViewForUid(uid))

                                postViewEvent(CallingViewState.MuteOrUnmuteRemoteViewForUid(uid, isMuted))

                            }
                        }
                    }

                }

                is CallingStateEvent.OnStop -> {
                    MyApp.phoneCallActivityPaused()
                    if (isVideoCall) {
                        videoUids.keys.forEach { uid ->

                            //check if it's not muted
                            videoUids[uid]?.let { isMuted ->
                                if (!isMuted) {
                                    rtcEngine().muteRemoteVideoStream(uid, true)
                                }
                            }

                        }
                    }

                }

                is CallingStateEvent.OnWindowFocusChanged -> {
                    val visible = stateEvent.hasFocus
                    isCallActivityVisible = visible
                    if (visible) {
                        startListenForSensor()
                    } else {
                        stopListenForSensor()
                    }
                }


                is CallingStateEvent.AnswerIncoming -> {
                    fireCall?.let { fireCall ->
                        startCall(fireCall, true)
                    }
                }

                is CallingStateEvent.RejectIncoming -> {
                    rejectCall()
                }
                is CallingStateEvent.VolumeKeyPressed -> {
                    if (!isIncoming) {
                        ringtonePlayer?.stopRingtone()
                    }
                }

                is CallingStateEvent.UpdateMe -> {
                    postViewEvent(CallingViewState.UpdateCallingState(callingState))

                    if (isIncoming && hasAnswered) {
                        postViewEvent(CallingViewState.HideAnswerButtons)
                    }

                    if (isLocalVideoEnabled) {
                        postViewEvent(CallingViewState.SetupLocalView)
                        postViewEvent(CallingViewState.ResumeLocalVideo)
                    } else {
                        postViewEvent(CallingViewState.PauseLocalVideo)
                    }

                    if (isSpeakerEnabled) {
                        postViewEvent(CallingViewState.EnableSpeaker)
                    } else {
                        postViewEvent(CallingViewState.DisableSpeaker)
                    }

                    postViewEvent(CallingViewState.MicMuted(isMicMuted))

                    videoUids.keys.forEach { uid ->

                        if (videoUids[uid] == false) {
                            postViewEvent(CallingViewState.SetupRemoteViewForUid(uid))
                            postViewEvent(CallingViewState.MuteOrUnmuteRemoteViewForUid(uid, false))
                        } else {
                            postViewEvent(CallingViewState.MuteOrUnmuteRemoteViewForUid(uid, true))
                        }

                    }


                }
            }
        }


    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("UpdateUIUpdateCalling", "Service    onTaskRemoved    ====     ")
        super.onTaskRemoved(rootIntent)
        stop()
    }

}
