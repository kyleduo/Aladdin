package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

/**
 * @author kyleduo on 2021/6/12
 */
@Suppress("unused")
class GenieConfigurator {
    val genies = mutableListOf<AladdinGenie>()

    fun addGenie(genie: AladdinGenie): GenieConfigurator {
        genies.add(genie)
        return this
    }

    fun prependGenie(genie: AladdinGenie): GenieConfigurator {
        genies.add(0, genie)
        return this
    }
}