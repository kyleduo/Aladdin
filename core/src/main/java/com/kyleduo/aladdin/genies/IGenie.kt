package com.kyleduo.aladdin.genies

/**
 * An [IGenie] is a functional component providing specific features for a domain.
 *
 * @author kyleduo on 2021/5/18
 */
interface IGenie {
    /**
     * a unique key for this [IGenie].
     */
    val key: String

    /**
     * Lifecycle callback for start.
     * This function would be invoked after Aladdin has been initialized properly.
     */
    fun onStart()

    /**
     * Lifecycle callback for stop.
     * This function would be invoked when Aladdin is going to be stopped.
     */
    fun onStop()
}
