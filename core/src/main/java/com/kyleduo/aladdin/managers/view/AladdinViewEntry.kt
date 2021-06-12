package com.kyleduo.aladdin.managers.view

import com.kyleduo.aladdin.api.manager.view.AladdinView
import com.kyleduo.aladdin.api.manager.view.AladdinViewAgent

/**
 * @author kyleduo on 2021/6/12
 */
class AladdinViewEntry(
    val view: AladdinView,
    val agent: AladdinViewAgent,
    var hasAgentBound: Boolean
)