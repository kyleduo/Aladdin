package com.kyleduo.aladdin.api

import android.app.Application
import com.kyleduo.aladdin.api.config.GenieConfigurator
import com.kyleduo.aladdin.api.config.LifecycleConfigurator
import com.kyleduo.aladdin.api.config.ViewConfigurator

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

    fun lifecycle(configBlock: ((LifecycleConfigurator) -> Unit)): AladdinConfigurator {
        configBlock(ensureConfigurator())
        return this
    }

    fun view(configBlock: ((ViewConfigurator) -> Unit)): AladdinConfigurator {
        configBlock(ensureConfigurator())
        return this
    }

    fun genie(configBlock: ((GenieConfigurator) -> Unit)): AladdinConfigurator {
        configBlock(ensureConfigurator())
        return this
    }

    @Suppress("unused")
    fun install() {
        Aladdin.install(this)
    }
}