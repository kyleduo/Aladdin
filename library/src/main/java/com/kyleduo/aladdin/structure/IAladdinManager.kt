package com.kyleduo.aladdin.structure

/**
 * General interface for a manager.
 *
 * @author kyleduo on 2021/5/18
 */
interface IAladdinManager {

    /**
     * Invoked while managers was being install.
     */
    fun attach()

    /**
     * Invoked when all managers has been installed properly.
     */
    fun ready()
}
