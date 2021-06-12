package com.kyleduo.aladdin.api

/**
 * @author kyleduo on 2021/6/12
 */
interface AladdinContextFactory {

    fun createAladdinContext(configurator: AladdinConfigurator) : AladdinContext
}