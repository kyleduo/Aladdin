package com.kyleduo.aladdin

import android.app.Application

/**
 * Entry point of Aladdin library.
 * Install and enable aladdin by adding lines to [Application.onCreate], here's an basic example:
 *
 * ```
 * fun onCreate() {
 *   super.onCreate()
 *   Aladdin.with(this).install()
 * }
 * ```
 *
 * @author kyleduo on 2021/5/18
 */
class Aladdin(private val application: Application) {
    companion object {
        fun with(application: Application): Aladdin {
            return Aladdin(application)
        }
    }

    fun install() {
        // install and enable aladdin instance to application
    }
}
