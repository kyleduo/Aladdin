package com.kyleduo.aladdin.lifecycle

/**
 * @author kyleduo on 2021/5/18
 */
interface OnAppBackgroundListener {

    fun onEnterBackground()
}

/**
 * @author kyleduo on 2021/5/18
 */
interface OnAppForegroundListener {

    fun onEnterForeground()
}