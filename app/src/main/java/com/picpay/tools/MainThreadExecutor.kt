package com.picpay.tools

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/*
* Created By neomatrix on 2019-07-09
*/
class MainThreadExecutor  : Executor {
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
        mainThreadHandler.post(command)
    }
}

fun getMainThreadExecutor(): Executor {
    return MainThreadExecutor()
}

fun getBackGroundThreadExecutor(): Executor {
    return Executors.newSingleThreadExecutor()
}