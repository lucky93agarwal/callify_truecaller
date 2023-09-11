package com.gpslab.kaun.calling
enum class CallEndedReason {
    UNKNOWN,
    LOCAL_HUNG_UP,
    LOCAL_REJECTED,
    ERROR,
    REMOTE_HUNG_UP,
    REMOTE_REJECTED,
    NO_ANSWER
}