package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.AladdinConfigurator

/**
 * @author kyleduo on 2021/6/12
 */
abstract class AbstractManagerConfigurator(
    private val aladdinConfigurator: AladdinConfigurator
) {

    fun end(): AladdinConfigurator {
        return aladdinConfigurator
    }
}