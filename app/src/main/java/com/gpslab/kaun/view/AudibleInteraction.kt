package com.gpslab.kaun.view

interface AudibleInteraction {
    fun onSeek(message:Message,progress:Int,max:Int)
}