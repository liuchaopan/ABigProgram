package com.pan.abigprogram.di

import androidx.fragment.app.FragmentActivity
import com.sina.weibo.sdk.auth.sso.SsoHandler
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

/**
 * Created by liuchaopan on 2019/5/23.
 */
const val SINA_MODULE_TAG = "SINA_MODULE_TAG"

val sinaKodeinModule = Kodein.Module(LOGIN_MODULE_TAG) {

    bind<SsoHandler>() with scoped<FragmentActivity>(AndroidLifecycleScope).singleton {
        SsoHandler(context)
    }
}