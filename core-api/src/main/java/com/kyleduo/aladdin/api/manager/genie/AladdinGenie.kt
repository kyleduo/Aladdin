package com.kyleduo.aladdin.api.manager.genie

import com.kyleduo.aladdin.api.AladdinContext

/**
 * An [AladdinGenie] is a functional component providing specific features for a domain.
 *
 * @author kyleduo on 2021/5/18
 */
abstract class AladdinGenie(
    val context: AladdinContext
) {
    /**
     * a unique key for this [AladdinGenie].
     */
    abstract val key: String

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
