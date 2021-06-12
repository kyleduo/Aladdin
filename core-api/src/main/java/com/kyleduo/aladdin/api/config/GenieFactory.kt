package com.kyleduo.aladdin.api.config

import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

/**
 * @author kyleduo on 2021/6/12
 */
fun interface GenieFactory {

    fun create(context: AladdinContext): AladdinGenie
}