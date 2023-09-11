package com.gpslab.kaun.status

import io.reactivex.disposables.CompositeDisposable

interface Base {
    val disposables: CompositeDisposable
}