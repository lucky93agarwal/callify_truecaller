package com.gpslab.kaun.calling

import android.view.SurfaceView

sealed class CallingViewState {
    class UpdateDuration(val duration: Long) : CallingViewState()
    class JoinChannelSuccess(val uid: Int) : CallingViewState()
    object PauseLocalVideo : CallingViewState()
    object ResumeLocalVideo : CallingViewState()
    object EnableSpeaker : CallingViewState()
    object DisableSpeaker : CallingViewState()
    class MicMuted(val setMuted: Boolean) : CallingViewState()
    class SetupRemoteViewForUid(val uid: Int) : CallingViewState()
    class SetupRemoteViewWithSurfaceView(val uid: Int, val surfaceView: SurfaceView) : CallingViewState()
    class RemoveRemoteViewForUid(val uid: Int) : CallingViewState()
    object HideRemoteViews : CallingViewState()
    class MuteOrUnmuteRemoteViewForUid(val uid: Int, val mute: Boolean) : CallingViewState()
    class UpdateCallingState(val callingState: CallingState) : CallingViewState()
    object OnCallEstablished : CallingViewState()
    object HideAnswerButtons : CallingViewState()

    object SetupLocalView : CallingViewState()

    class CallEnded(val reason: CallEndedReason) : CallingViewState()
}