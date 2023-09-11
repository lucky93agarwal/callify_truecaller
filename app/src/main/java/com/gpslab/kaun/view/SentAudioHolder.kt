package com.gpslab.kaun.view

import ak.sh.ay.musicwave.MusicWave
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.github.abdularis.buttonprogress.DownloadButtonProgress
import com.gpslab.kaun.R
import pl.droidsonroids.gif.GifImageView


class SentAudioHolder(context: Context, itemView: View) : BaseSentHolder(context, itemView), AudibleBase {

    var waveView: MusicWave = itemView.findViewById(R.id.wave_view)
    var progressbar: DownloadButtonProgress = itemView.findViewById(R.id.progress_button)
    var playBtn: ImageView = itemView.findViewById(R.id.voice_play_btn)

    var pauseBtn: ImageView = itemView.findViewById(R.id.voice_puse_btn)
    var seekBar: SeekBar = itemView.findViewById(R.id.voice_seekbar)
    private val tvAudioSize: TextView = itemView.findViewById(R.id.tv_audio_size)
    var tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
    var imgHeadset: ImageView = itemView.findViewById(R.id.img_headset)
    var gifimg: GifImageView = itemView.findViewById(R.id.wave_viewgif)

    private lateinit var runnable: Runnable
    var playcheck: Boolean = false
    var progress_in_persentage: Int = 0
    private var handler: Handler = Handler()
    private lateinit var mediaPlayers: MediaPlayer

    private var pause: Boolean = false
    private var check_pause: Boolean = false


    private var checkclick: Boolean = false
    private var progresscheckclick: Boolean = false
    override var audibleState: LiveData<Map<String, AudibleState>>? = null
    override var audibleInteraction: AudibleInteraction? = null


    val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }

    // Creating an extension property to get media player current position in seconds
    val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / 1000
        }

    override fun bind(message: Message, user: User) {
        super.bind(message, user)


        //Set Initial Values
        seekBar.progress = 0
        playBtn.setImageResource(AdapterHelper.getPlayIcon(false))

        //if it's sending set the audio size
//        if (message.downloadUploadStat != DownloadUploadStat.SUCCESS) {
        tvAudioSize.visibility = View.VISIBLE
        tvAudioSize.text = message.metadata
//        } else {
//            //otherwise hide the audio textview
//            tvAudioSize.visibility = View.GONE
//        }


//        if (message.fromPhone.equals("1")) {
//            imgHeadset.visibility = View.VISIBLE
//            gifimg.visibility = View.GONE
//        } else {
//            imgHeadset.visibility = View.GONE
//            gifimg.visibility = View.VISIBLE
//        }




        tvDuration.text = message.mediaDuration



        playBtn.setHidden(message.downloadUploadStat != DownloadUploadStat.SUCCESS, true)
        lifecycleOwner?.let {
            Log.d("PlayButtonclick", "onClick   messageId = " + message.messageId)

            audibleState?.observe(it, Observer { audioRecyclerStateMap ->
                if (audioRecyclerStateMap.containsKey(message.messageId)) {
                    audioRecyclerStateMap[message.messageId]?.let { mAudioRecyclerState ->

                        Log.d("PlayButtonclick", "onClick   messageId = " + message.messageId)
                        Log.d("PlayButtonclick", "onClick   currentDuration = " + mAudioRecyclerState.currentDuration)
                        if (mAudioRecyclerState.currentDuration != null)
                            tvDuration.text = mAudioRecyclerState.currentDuration

                        if (mAudioRecyclerState.progress != -1) {
                            seekBar.progress = mAudioRecyclerState.progress
                        }

                        if (mAudioRecyclerState.max != -1) {
                            val max = mAudioRecyclerState.max
                            seekBar.max = max
                        }

                        if (mAudioRecyclerState.isPlaying) {
                            imgHeadset.visibility = View.GONE
                            waveView.visibility = View.VISIBLE
                        } else {
                            imgHeadset.visibility = View.VISIBLE
                            waveView.visibility = View.GONE
                        }

                        if (mAudioRecyclerState.waves != null)
                            waveView.updateVisualizer(mAudioRecyclerState.waves)

                        playBtn.setImageResource(AdapterHelper.getPlayIcon(mAudioRecyclerState.isPlaying))

                    }
                } else {
                    tvDuration.text = message.mediaDuration
                    imgHeadset.visibility = View.VISIBLE
                    waveView.visibility = View.GONE
                }
            })
        }
        pauseBtn.setOnClickListener {
            if (mediaPlayers.isPlaying) {
                mediaPlayers.pause()

                imgHeadset.visibility = View.VISIBLE
                gifimg.visibility = View.GONE
                pauseBtn.visibility = View.GONE
                pause = true

            }
        }
        playBtn.setOnClickListener {
//            Log.d("PlayButtonclick", "seekBar   = " + check_pause)
//           if (mediaPlayers.isPlaying){
//               mediaPlayers.stop()
//               mediaPlayers.release()
//           }

            val myUri: Uri = Uri.parse(message.localPath) // initialize Uri here
            if (pause) {
                mediaPlayers.seekTo(mediaPlayers.currentPosition)
                mediaPlayers.start()
                pause = false
                imgHeadset.visibility = View.VISIBLE
                gifimg.visibility = View.GONE
            } else {

                if (checkclick) {
//                    message.fromPhone ="1"
                    mediaPlayers.stop()
                    mediaPlayers.release()

                    mediaPlayers = MediaPlayer.create(context, myUri)
                    mediaPlayers.start()
                    Log.d("PlayButtonclick", "seekBar 2   = " + check_pause)
                    imgHeadset.visibility = View.GONE
                    gifimg.visibility = View.VISIBLE
                } else {
                    Log.d("PlayButtonclick", "seekBar 0   = " + check_pause)
                    checkclick = true
                    Log.d("PlayButtonclick", "seekBar 1   = " + check_pause)

                    mediaPlayers = MediaPlayer.create(context, myUri)
                    mediaPlayers.start()

                    imgHeadset.visibility = View.GONE
                    gifimg.visibility = View.VISIBLE
                }


            }

            pauseBtn.visibility = View.VISIBLE
            mediaPlayers.setOnCompletionListener {


            }
            seekBar.max = mediaPlayers.seconds

            runnable = Runnable {
                seekBar.progress = mediaPlayers.currentSeconds




                handler.postDelayed(runnable, 1000)
            }
            handler.postDelayed(runnable, 1000)


        }

        progressbar.setOnClickListener {


//            progressbar.setCurrentProgress(100)
//            progressbar.setFinish()
            val sharedPre: SharedPreferences = context.getSharedPreferences("ChatData", 0)
            val edit = sharedPre.edit()
            progresscheckclick = sharedPre.getBoolean("check_progress", false)
            Log.d("PlayButtonclick", "progresscheckclick 2   = " + progresscheckclick)
            if(progresscheckclick){
                Log.d("PlayButtonclick", "progresscheckclick 4   = " + progresscheckclick)
                progresscheckclick = false
                edit.putBoolean("check_progress", false)
                edit.putBoolean("check_pro", false)
                edit.putInt("pause_and_resume", 2)
                edit.apply()
                progressbar.setDeterminate()

            }else {
                Log.d("PlayButtonclick", "progresscheckclick 3   = " + progresscheckclick)

                progresscheckclick = true
                progressbar.setIdle()
                edit.putInt("pause_and_resume", 1)
                edit.putBoolean("check_progress", true)
                edit.putBoolean("check_pro", true)
                edit.apply()

            }


        }
        object : CountDownTimer(5000, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                val sharedPre: SharedPreferences = context.getSharedPreferences("ChatData", 0)
                val edit = sharedPre.edit()

                progress_in_persentage = sharedPre.getInt("progress_in_per", 100)
//                Log.d("PlayButtonclick", "CountDownTimer check_pro 3   = " + progress_in_persentage)
                Log.d("PlayButtonclick", "CountDownTimer check_pro 3   = " + sharedPre.getBoolean("check_pro", false))
                Log.d("PlayButtonclick", "CountDownTimer check_progress 3   = " + sharedPre.getBoolean("check_progress", false))
                if(progress_in_persentage == 100){
                    edit.putBoolean("check_progress", false)
                    edit.apply()
                    progressbar.setIdle()
                    progresscheckclick = true

//                    Log.d("PlayButtonclick", "CountDownTimer progress_in_persentage 2   = " + progress_in_persentage)

                }else {
                    if(!sharedPre.getBoolean("check_pro", false)){
//                        if(!sharedPre.getBoolean("check_progress",false)){
                            Log.d("PlayButtonclick", "CountDownTimer progress_in_persentage 4   = " + progress_in_persentage)
                            progressbar.setIdle()
//                        }
                    }else {
//                        Log.d("PlayButtonclick", "CountDownTimer progress_in_persentage 1  = " + progress_in_persentage)
                        progressbar.setDeterminate()
                        progressbar.setCurrentProgress(progress_in_persentage)
                    }
                }
            }

            override fun onFinish() {

            }
        }.start()




        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                Log.d("PlayButtonclick", "seekBar   mediaDuration = " + message)
//                Log.d("PlayButtonclick", "seekBar   mediaDuration = " + progress)
//                Log.d("PlayButtonclick", "seekBar   mediaDuration = " + seekBar.max)
                if (fromUser) {
                    mediaPlayers.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }


}
