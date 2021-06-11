package com.kyleduo.aladdin.lifecycle

/**
 * @author kyleduo on 2021/5/18
 */
interface AladdinAppLifecycleCallbacks {
    fun onAppEnterForeground()

    fun onAppEnterBackground()
}