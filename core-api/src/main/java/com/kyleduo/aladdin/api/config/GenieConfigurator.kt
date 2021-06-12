package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.AladdinConfigurator

/**
 * @author kyleduo on 2021/6/12
 */
class GenieConfigurator(aladdinConfigurator: AladdinConfigurator) :
    AbstractManagerConfigurator(aladdinConfigurator) {
    val genieFactories = mutableListOf<GenieFactory>()

    fun addGenie(genieFactory: GenieFactory): GenieConfigurator {
        genieFactories.add(genieFactory)
        return this
    }

    fun prependGenie(genieFactory: GenieFactory): GenieConfigurator {
        genieFactories.add(0, genieFactory)
        return this
    }
}