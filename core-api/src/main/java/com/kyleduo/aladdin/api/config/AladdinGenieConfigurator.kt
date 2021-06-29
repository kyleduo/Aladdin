package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

/**
 * @author kyleduo on 2021/6/12
 */
@Suppress("unused")
class AladdinGenieConfigurator {
    val genies = mutableListOf<AladdinGenie>()

    fun addGenie(genie: AladdinGenie): AladdinGenieConfigurator {
        genies.add(genie)
        return this
    }

    fun prependGenie(genie: AladdinGenie): AladdinGenieConfigurator {
        genies.add(0, genie)
        return this
    }
}