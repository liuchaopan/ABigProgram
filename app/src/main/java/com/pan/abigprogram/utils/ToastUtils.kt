package com.pan.abigprogram.utils

import com.pan.abigprogram.PanApplication
import com.pan.abigprogram.ext.toast


inline fun toast(value: () -> String): Unit =
        PanApplication.INSTANCE.toast(value)