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

    private val onAppForegroundListeners = mutableListOf<OnAppForegroundListener>()
    private val onAppBackgroundListeners = mutableListOf<OnAppBackgroundListener>()

    override fun attach() {
        Aladdin.context.application.registerActivityLifecycleCallbacks(this)
    }

    fun addOnAppForegroundListener(onAppForegroundListener: OnAppForegroundListener) {
        if (onAppForegroundListeners.contains(onAppForegroundListener)) {
            onAppForegroundListeners.add(onAppForegroundListener)
        }
    }

    @Suppress("unused")
    fun removeOnAppForegroundListener(onAppForegroundListener: OnAppForegroundListener) {
        onAppForegroundListeners.remove(onAppForegroundListener)
    }

    fun addOnAppBackgroundListener(onAppBackgroundListener: OnAppBackgroundListener) {
        if (onAppBackgroundListeners.contains(onAppBackgroundListener)) {
            onAppBackgroundListeners.add(onAppBackgroundListener)
        }
    }

    @Suppress("unused")
    fun removeOnAppBackgroundListener(onAppBackgroundListener: OnAppBackgroundListener) {
        onAppBackgroundListeners.remove(onAppBackgroundListener)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        startedActivityCount += 1
        if (startedActivityCount == 1) {
            onAppForegroundListeners.forEach {
                it.onEnterForeground()
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        startedActivityCount -= 1
        if (startedActivityCount == 0) {
            onAppBackgroundListeners.forEach {
                it.onEnterBackground()
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
