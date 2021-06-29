package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.manager.view.AladdinViewMode

/**
 * @author kyleduo on 2021/6/12
 */
@Suppress("unused")
class AladdinViewConfigurator {

    var viewMode: AladdinViewMode? = null

    fun viewMode(mode: AladdinViewMode): AladdinViewConfigurator {
        this.viewMode = mode
        return this
    }
}