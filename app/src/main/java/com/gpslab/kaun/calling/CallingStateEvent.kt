package com.gpslab.kaun.calling

import android.view.SurfaceView
import com.gpslab.kaun.view.FireCall

sealed class CallingStateEvent {

    object SpeakerClicked : CallingStateEvent()
    object MicClicked : CallingStateEvent()
    object BtnVideoClicked : CallingStateEvent()
    object FlipCameraClicked : CallingStateEvent()
    object EndCall : CallingStateEvent()
    object UpdateMe : CallingStateEvent()
    object OnStop : CallingStateEvent()
    object OnStart : CallingStateEvent()
    class OnWindowFocusChanged(val hasFocus: Boolean) : CallingStateEvent()
    class SurfaceViewAddedForUid(val uid: Int, val surfaceV: SurfaceView) : CallingStateEvent()
    object AnswerIncoming : CallingStateEvent()
    object RejectIncoming : CallingStateEvent()
    object VolumeKeyPressed : CallingStateEvent()

    class StartCall(val fireCall: FireCall, val isIncoming: Boolean) : CallingStateEvent()

}