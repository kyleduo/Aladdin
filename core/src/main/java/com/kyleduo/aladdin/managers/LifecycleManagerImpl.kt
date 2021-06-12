package com.kyleduo.aladdin.managers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.config.LifecycleConfigurator
import com.kyleduo.aladdin.api.manager.lifecycle.AppLifecycleCallbacks
import com.kyleduo.aladdin.api.manager.lifecycle.LifecycleManager

/**
 * Listening activity lifecycle event and expose that.
 *
 * @author kyleduo on 2021/5/18
 */
class LifecycleManagerImpl(
    val context: AladdinContext,
    lifecycle: LifecycleConfigurator?
) : LifecycleManager, Application.ActivityLifecycleCallbacks {

    private var startedActivityCount = 0

    private val appLifecycleCallbacks = mutableListOf<AppLifecycleCallbacks>()
    private val activityLifecycleCallbacks = mutableListOf<Application.ActivityLifecycleCallbacks>()

    override fun attach() {
        context.app.registerActivityLifecycleCallbacks(this)
    }

    override fun ready() {

    }

    override fun registerAppLifecycleCallbacks(callback: AppLifecycleCallbacks) {
        if (callback !in appLifecycleCallbacks) {
            appLifecycleCallbacks.add(callback)
        }
    }

    override fun registerActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
        if (callback !in activityLifecycleCallbacks) {
            activityLifecycleCallbacks.add(callback)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityLifecycleCallbacks.forEach {
            it.onActivityCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        activityLifecycleCallbacks.forEach {
            it.onActivityStarted(activity)
        }
        startedActivityCount += 1
        if (startedActivityCount == 1) {
            appLifecycleCallbacks.forEach {
                it.onAppEnterForeground()
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        activityLifecycleCallbacks.forEach {
            it.onActivityResumed(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        activityLifecycleCallbacks.forEach {
            it.onActivityPaused(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        activityLifecycleCallbacks.forEach {
            it.onActivityStopped(activity)
        }

        startedActivityCount -= 1
        if (startedActivityCount == 0) {
            appLifecycleCallbacks.forEach {
                it.onAppEnterBackground()
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        activityLifecycleCallbacks.forEach {
            it.onActivitySaveInstanceState(activity, outState)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityLifecycleCallbacks.forEach {
            it.onActivityDestroyed(activity)
        }
    }
}
