package com.kyleduo.aladdin.api.manager.lifecycle

/**
 * @author kyleduo on 2021/5/18
 */
interface AppLifecycleCallbacks {
    fun onAppEnterForeground()

    fun onAppEnterBackground()
}