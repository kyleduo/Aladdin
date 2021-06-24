package com.kyleduo.aladdin.api

import android.app.Application
import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

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

    lateinit var context: AladdinContext

    fun with(application: Application): AladdinConfigurator {
        return AladdinConfigurator(application)
    }

    inline fun <reified Genie : AladdinGenie> findGenie(key: String): Genie? {
        return context.genieManager.findGenie(key) as? Genie
    }
}
