package com.kyleduo.aladdin.view

import com.kyleduo.aladdin.view.agent.IAladdinViewAgent

/**
 * @auther kyleduo on 2021/6/12
 */
class AladdinViewEntry(
    val view: IAladdinView,
    val agent: IAladdinViewAgent,
    var hasAgentBound: Boolean
)