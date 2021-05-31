package com.kyleduo.aladdin.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.structure.IAladdinManager

/**
 * Listening activity lifecycle event and expose that.
 *
 * @author kyleduo on 2021/5/18
 */
class ActivityLifecycleManager : IAladdinManager, Application.ActivityLifecycleCallbacks {

    private var startedActivityCount = 0

    private val appLifecycleCallbacks = mutableListOf<AladdinAppLifecycleCallbacks>()
    private val activityLifecycleCallbacks = mutableListOf<Application.ActivityLifecycleCallbacks>()

    override fun attach() {
        Aladdin.context.application.registerActivityLifecycleCallbacks(this)
    }

    override fun ready() {

    }

    fun registerAppLifecycleCallbacks(callback: AladdinAppLifecycleCallbacks) {
        if (callback !in appLifecycleCallbacks) {
            appLifecycleCallbacks.add(callback)
        }
    }

    fun registerActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
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
