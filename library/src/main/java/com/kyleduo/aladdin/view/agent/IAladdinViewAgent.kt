package com.kyleduo.aladdin.view.agent

import com.kyleduo.aladdin.view.IAladdinView

/**
 * Agent of a view, providing abilities like show/dismiss, positioning, resizing of a view.
 *
 * @author kyleduo on 2021/5/28
 */
interface IAladdinViewAgent {

    /**
     * Bind a view to this agent.
     * All other operation should happened after binding.
     */
    fun bind(view: IAladdinView)

    /**
     * resize the bound view
     */
    fun resize(width: Int, height: Int)

    /**
     * set gravity of the view
     */
    fun gravity(gravity: Int)

    /**
     * move to a specific position
     */
    fun moveTo(x: Int, y: Int)

    /**
     * move by a delta distance.
     */
    fun moveBy(dx: Int, dy: Int)

    /**
     * show the view
     */
    fun show()

    /**
     * dismiss the view
     */
    fun dismiss()
}
