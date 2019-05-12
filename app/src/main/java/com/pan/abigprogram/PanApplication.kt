package com.pan.abigprogram

import android.app.Application
import android.content.Context
import com.pan.library.handler.CrashHandler
import com.pan.abigprogram.di.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class PanApplication : Application() , KodeinAware {

    companion object {
        lateinit var INSTANCE: PanApplication
    }

    override val kodein: Kodein = Kodein.lazy {
        bind<Context>() with singleton { this@PanApplication }
        import(androidCoreModule(this@PanApplication))
        import(androidXModule(this@PanApplication))

        import(serviceModule)
        import(dbModule)
        import(httpClientModule)
        import(serializableModule)
        import(globalRepositoryModule)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        CrashHandler.instance.init()
    }



}