package com.kyleduo.aladdin.api.manager.genie

import com.kyleduo.aladdin.api.AladdinContext

/**
 * An [AladdinGenie] is a functional component providing specific features for a domain.
 *
 * @author kyleduo on 2021/5/18
 */
abstract class AladdinGenie {

    /**
     * If this Genie is accessible in runtime, since we do not want any implementations of Genies
     * run in release, so it's needed to declare a interface and override this property returning
     * that interface's class.
     */
    open val apiClass: Class<*> = this::class.java

    /**
     * [AladdinContext] instance. Injected before [onStart]
     */
    lateinit var context: AladdinContext

    /**
     * Lifecycle callback for start.
     * This function would be invoked after Aladdin has been initialized properly.
     */
    abstract fun onStart()

    /**
     * Lifecycle callback for stop.
     * This function would be invoked when Aladdin is going to be stopped.
     */
    abstract fun onStop()
}
