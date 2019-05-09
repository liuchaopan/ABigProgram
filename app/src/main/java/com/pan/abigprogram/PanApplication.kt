package com.pan.abigprogram

import android.app.Application

class PanApplication : Application() {

    fun getInstance(): PanApplication {
        return this
    }


}