package com.pan.abigprogram.utils

import com.google.gson.Gson
import com.pan.abigprogram.PanApplication
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

object GsonUtils : KodeinAware {

    override val kodein: Kodein
        get() = PanApplication.INSTANCE.kodein

    val INSTANCE: Gson by instance()
}

fun <T> T.toJson(): String {
    return GsonUtils.INSTANCE.toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return GsonUtils.INSTANCE.fromJson(this, T::class.java)
}