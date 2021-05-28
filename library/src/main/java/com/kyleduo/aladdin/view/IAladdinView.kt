package com.kyleduo.aladdin.view

import android.view.View
import com.kyleduo.aladdin.view.agent.IAladdinViewAgent

/**
 * An [IAladdinView] represents a View component of Aladdin, that is a "global" component belong to
 * Aladdin but not any [com.kyleduo.aladdin.genies.IGenie]
 *
 * Typically there will be two [IAladdinView]s which are "entry" and "board".
 *
 * @author kyleduo on 2021/5/25
 */
abstract class IAladdinView {

    abstract val view: View
    abstract val tag: Any

    protected lateinit var agent: IAladdinViewAgent

    fun bindAgent(agent: IAladdinViewAgent) {
        this.agent = agent
        agent.bind(this)
        this.onAgentBound()
    }

    /**
     * An [IAladdinViewAgent] has been bound to this view.
     * The view can communicate to Aladdin since now.
     */
    open fun onAgentBound() {

    }
}
