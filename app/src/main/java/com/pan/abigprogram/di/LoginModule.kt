package com.pan.abigprogram.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.pan.abigprogram.data.LoginDataSource
import com.pan.abigprogram.data.LoginRepository
import com.pan.abigprogram.ui.login.LoginViewModel
import com.pan.abigprogram.ui.login.LoginViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

const val LOGIN_MODULE_TAG = "LOGIN_MODULE_TAG"

val loginKodeinModule = Kodein.Module(LOGIN_MODULE_TAG) {

    bind<LoginViewModel>() with scoped<FragmentActivity>(AndroidLifecycleScope).singleton {
        ViewModelProviders
                .of(context, LoginViewModelFactory.getInstance(instance()))
                .get(LoginViewModel::class.java)
    }


    bind<LoginDataSource>() with singleton {
        LoginDataSource(instance(), instance())
    }

    bind<LoginRepository>() with singleton {
        LoginRepository(instance())
    }
}