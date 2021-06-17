package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.AladdinConfigurator
import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

/**
 * @author kyleduo on 2021/6/12
 */
class GenieConfigurator(aladdinConfigurator: AladdinConfigurator) :
    AbstractManagerConfigurator(aladdinConfigurator) {
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