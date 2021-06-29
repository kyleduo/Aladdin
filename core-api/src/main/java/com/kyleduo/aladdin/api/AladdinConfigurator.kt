package com.kyleduo.aladdin.api

import android.app.Application
import com.kyleduo.aladdin.api.config.AladdinGenieConfigurator
import com.kyleduo.aladdin.api.config.AladdinLifecycleConfigurator
import com.kyleduo.aladdin.api.config.AladdinViewConfigurator

/**
 * Configuration entry for Aladdin.
 *
 * @author kyleduo on 2021/6/12
 */
class AladdinConfigurator(val application: Application) {

    private val configurators =
        mutableMapOf<Class<out Any>, Any>()

    fun <T> getConfigurator(clazz: Class<T>): T? {
        @Suppress("UNCHECKED_CAST", "TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
        return configurators[clazz] as? T
    }

    private inline fun <reified T : Any> ensureConfigurator(): T {
        return (configurators[T::class.java] ?: T::class.java.newInstance().also {
            configurators[T::class.java] = it
        }) as T
    }

    fun lifecycle(configBlock: ((AladdinLifecycleConfigurator) -> Unit)): AladdinConfigurator {
        configBlock(ensureConfigurator())
        return this
    }

    fun view(configBlock: ((AladdinViewConfigurator) -> Unit)): AladdinConfigurator {
        configBlock(ensureConfigurator())
        return this
    }

    fun genie(configBlock: ((AladdinGenieConfigurator) -> Unit)): AladdinConfigurator {
        configBlock(ensureConfigurator())
        return this
    }

    @Suppress("unused")
    fun install() {
        Aladdin.install(this)
    }
}