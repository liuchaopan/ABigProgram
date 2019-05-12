package com.pan.abigprogram.di

import com.pan.abigprogram.http.service.LoginService
import com.pan.abigprogram.http.service.ServiceManager
import com.pan.abigprogram.http.service.UserService

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

private const val SERVICE_MODULE_TAG = "serviceModule"

val serviceModule = Kodein.Module(SERVICE_MODULE_TAG) {

    bind<UserService>() with singleton {
        instance<Retrofit>().create(UserService::class.java)
    }

    bind<LoginService>() with singleton {
        instance<Retrofit>().create(LoginService::class.java)
    }

    bind<ServiceManager>() with singleton {
        ServiceManager(instance(), instance())
    }
}