package com.pan.abigprogram.di

import android.content.Context
import android.content.SharedPreferences
import com.pan.abigprogram.PanApplication
import com.pan.abigprogram.repository.UserInfoRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

private const val GLOBAL_REPOS_MODULE_TAG = "PrefsModule"

private const val DEFAULT_SP_TAG = "PrefsDefault"

val globalRepositoryModule = Kodein.Module(GLOBAL_REPOS_MODULE_TAG) {

    bind<SharedPreferences>(DEFAULT_SP_TAG) with singleton {
        PanApplication.INSTANCE.getSharedPreferences(DEFAULT_SP_TAG, Context.MODE_PRIVATE)
    }

    bind<UserInfoRepository>() with singleton {
        UserInfoRepository.getInstance(instance(DEFAULT_SP_TAG))
    }
}