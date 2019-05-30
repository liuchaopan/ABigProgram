package tech.soit.quiet

import android.app.Application
import com.pan.library.handler.CrashHandler
import tech.soit.quiet.utils.component.AppTask

/**
 * application context
 */
class AppContext : Application() {

    /**
     * singleton for application
     */
    companion object : Application()

    override fun onCreate() {
        super.onCreate()
        AppContext.attachBaseContext(this)
        AppContext.setTheme(R.style.AppTheme)
        CrashHandler.instance.init()
        registerActivityLifecycleCallbacks(AppTask.CallBack)
    }
}