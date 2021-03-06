package com.kyleduo.aladdin.api

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
@Suppress("unused")
object Aladdin {
    private const val CONTEXT_IMPL_CLASS_NAME = "com.kyleduo.aladdin.AladdinContextImpl"

    var context: AladdinContext? = null
        private set

    fun with(application: Application): AladdinConfigurator {
        return AladdinConfigurator(application)
    }

    internal fun install(configurator: AladdinConfigurator) {
        val contextImplClass = try {
            Class.forName(CONTEXT_IMPL_CLASS_NAME)
        } catch (e: ClassNotFoundException) {
            // AladdinContextImpl class is not found, probably in release mode, nothing happens.
            return
        }

        val context = try {
            contextImplClass
                .getDeclaredConstructor(AladdinConfigurator::class.java)
                .newInstance(configurator) as? AladdinContext
        } catch (e: Exception) {
            throw IllegalStateException("Fail to create AladdinContext.", e)
        }

        if (context !is AladdinContext) {
            throw IllegalStateException("AladdinContext class is not match.")
        }

        this.context = context
        context.install()
    }

    fun <Genie> findGenie(genieClass: Class<Genie>, key: String? = null): Genie? {
        return context?.genieManager?.findGenie(genieClass, key)
    }

    fun <Genie> withGenie(
        genieClass: Class<Genie>,
        key: String? = null,
        action: ((Genie) -> Unit)
    ) {
        context?.genieManager?.findGenie(genieClass, key)?.let {
            action(it)
        }
    }
}
