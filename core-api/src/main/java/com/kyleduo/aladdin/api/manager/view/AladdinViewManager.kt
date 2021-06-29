package com.kyleduo.aladdin.api.manager.view

import com.kyleduo.aladdin.api.manager.AladdinManager

/**
 * A manager for organizing [AladdinView]'s.
 *
 * @author kyleduo on 2021/6/12
 */
interface AladdinViewManager : AladdinManager {

    var mode: AladdinViewMode

    /**
     * Register an AladdinView to ViewManager
     */
    fun register(view: AladdinView)
}