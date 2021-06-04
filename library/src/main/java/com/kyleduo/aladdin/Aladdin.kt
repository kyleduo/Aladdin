package com.kyleduo.aladdin

import android.app.Application
import com.kyleduo.aladdin.genies.IGenie
import com.kyleduo.aladdin.preset.AppInfoGenie

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
@Suppress("unused")
object Aladdin {
    internal lateinit var context: AladdinContext

    fun with(application: Application): AladdinContext {
        return AladdinContext(application).also {
            context = it
            it.addGenie(AppInfoGenie())
        }
    }

    fun genie(key: String): IGenie? {
        return context.genieManager.findGenie(key)
    }
}
