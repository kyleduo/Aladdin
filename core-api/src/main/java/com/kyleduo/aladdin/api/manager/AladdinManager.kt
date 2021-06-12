package com.kyleduo.aladdin.api.manager

/**
 * General interface for a manager.
 *
 * @author kyleduo on 2021/5/18
 */
interface AladdinManager {

    /**
     * Invoked while managers was being install.
     */
    fun attach()

    /**
     * Invoked when all managers has been installed properly.
     */
    fun ready()
}
