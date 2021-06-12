package com.kyleduo.aladdin.api

import android.app.Application
import com.kyleduo.aladdin.api.config.AbstractManagerConfigurator
import com.kyleduo.aladdin.api.config.GenieConfigurator
import com.kyleduo.aladdin.api.config.LifecycleConfigurator
import com.kyleduo.aladdin.api.config.ViewConfigurator

/**
 * Configuration entry for Aladdin.
 *
 * @author kyleduo on 2021/6/12
 */
class AladdinConfigurator(
    val application: Application
) {

    private var aladdinContextFactory: AladdinContextFactory? = null
    private val configurators =
        mutableMapOf<Class<out AbstractManagerConfigurator>, AbstractManagerConfigurator>()

    fun <T> getConfigurator(clazz: Class<T>): T? {
        @Suppress("UNCHECKED_CAST", "TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
        return configurators[clazz] as? T
    }

    fun lifecycle(): LifecycleConfigurator {
        return LifecycleConfigurator(this).also {
            configurators[it::class.java] = it
        }
    }

    fun view(): ViewConfigurator {
        return ViewConfigurator(this).also {
            configurators[it::class.java] = it
        }
    }

    fun genie(): GenieConfigurator {
        return GenieConfigurator(this).also {
            configurators[it::class.java] = it
        }
    }

    fun contextFactory(aladdinContextFactory: AladdinContextFactory): AladdinConfigurator {
        this.aladdinContextFactory = aladdinContextFactory
        return this
    }

    fun install() {
        // instance context and set to aladdin
        if (aladdinContextFactory == null) {
            error("No AladdinContextFactory found.")
        }

        val context = aladdinContextFactory?.createAladdinContext(this)
            ?: error("Fail to create AladdinContext")

        Aladdin.context = context

        context.install()
    }
}