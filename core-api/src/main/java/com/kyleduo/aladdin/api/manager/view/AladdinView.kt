package com.kyleduo.aladdin.api.manager.view

import android.view.View

/**
 * An [AladdinView] represents a View component of Aladdin, that is a "global" component belong to
 * Aladdin but not any [com.kyleduo.aladdin.genies.IGenie]
 *
 * Typically there will be two [AladdinView]s which are "entry" and "board".
 *
 * @author kyleduo on 2021/5/25
 */
abstract class AladdinView {

    abstract val view: View
    abstract val tag: Any

    lateinit var agent: AladdinViewAgent

    fun bindAgent(agent: AladdinViewAgent) {
        this.agent = agent
        agent.bind(this)
        this.onAgentBound()
    }

    /**
     * An [AladdinViewAgent] has been bound to this view.
     * The view can communicate to Aladdin since now.
     */
    open fun onAgentBound() {

    }
}
