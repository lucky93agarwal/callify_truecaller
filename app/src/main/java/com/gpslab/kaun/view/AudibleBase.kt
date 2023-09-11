package com.gpslab.kaun.view

import androidx.lifecycle.LiveData

interface AudibleBase {
    var audibleState: LiveData<Map<String, AudibleState>>?
    var audibleInteraction:AudibleInteraction?
}