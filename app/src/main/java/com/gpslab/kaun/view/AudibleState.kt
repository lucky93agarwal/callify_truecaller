package com.gpslab.kaun.view
data class AudibleState (
        var isPlaying: Boolean = false,
        var currentDuration: String? = null,
        var waves:ByteArray?=null,
        var progress: Int = -1,
        var max: Int = -1
)