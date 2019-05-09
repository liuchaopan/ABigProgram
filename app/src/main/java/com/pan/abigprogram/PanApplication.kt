package com.pan.abigprogram

import android.app.Application
import com.pan.library.handler.CrashHandler

class PanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrashHandler.instance.init()
    }

    fun getInstance(): PanApplication {
        return this
    }


}