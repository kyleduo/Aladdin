package com.kyleduo.aladdin.api.manager.genie

import com.kyleduo.aladdin.api.AladdinContext

/**
 * An [AladdinGenie] is a functional component providing specific features for a domain.
 *
 * @author kyleduo on 2021/5/18
 */
abstract class AladdinGenie(
    /**
     * Used to distinguish this Genie from the others with same class.
     */
    val key: String = DEFAULT_KEY
) {

    companion object {

        const val DEFAULT_KEY = "default"
    }

    /**
     * If this Genie is accessible in runtime, since we do not want any implementations of Genies
     * run in release, so it's needed to declare a interface and override this property returning
     * that interface's class.
     */
    abstract val apiClass: Class<*>

    /**
     * [AladdinContext] instance. Injected before [onStart]
     */
    lateinit var context: AladdinContext

    /**
     * Whether support multiple genie instance or not.
     * If so, multiple instance can be added to Aladdin and they are specified by a "key".
     */
    open val isMultipleSupported: Boolean = false

    /**
     * Lifecycle callback for start.
     * This function would be invoked after Aladdin has been initialized properly.
     */
    open fun onStart() {}

    /**
     * Lifecycle callback for stop.
     * This function would be invoked when Aladdin is going to be stopped.
     */
    open fun onStop() {}
}
