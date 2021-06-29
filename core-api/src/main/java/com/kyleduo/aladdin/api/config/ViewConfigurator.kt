package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.manager.view.ViewMode

/**
 * @author kyleduo on 2021/6/12
 */
@Suppress("unused")
class ViewConfigurator {

    var viewMode: ViewMode? = null

    fun viewMode(mode: ViewMode): ViewConfigurator {
        this.viewMode = mode
        return this
    }
}