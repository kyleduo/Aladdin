package com.kyleduo.aladdin

import com.kyleduo.aladdin.api.AladdinConfigurator
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.AladdinContextFactory

/**
 * @author kyleduo on 2021/6/12
 */
class AladdinContextFactoryImpl : AladdinContextFactory {
    override fun createAladdinContext(configurator: AladdinConfigurator): AladdinContext {
        return AladdinContextImpl(configurator)
    }
}