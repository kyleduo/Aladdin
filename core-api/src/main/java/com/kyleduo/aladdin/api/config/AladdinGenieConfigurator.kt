package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

/**
 * @author kyleduo on 2021/6/12
 */
@Suppress("unused")
class AladdinGenieConfigurator {
    val genies = mutableListOf<Pair<AladdinGenie, String?>>()

    fun addGenie(genie: AladdinGenie, key: String? = null): AladdinGenieConfigurator {
        genies.add(genie to key)
        return this
    }

    fun prependGenie(genie: AladdinGenie, key: String? = null): AladdinGenieConfigurator {
        genies.add(0, genie to key)
        return this
    }
}