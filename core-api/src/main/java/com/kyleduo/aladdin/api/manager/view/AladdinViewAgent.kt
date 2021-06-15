package com.kyleduo.aladdin.api.manager.view

import android.util.Size

/**
 * Agent of a view, providing abilities like show/dismiss, positioning, resizing of a view.
 *
 * @author kyleduo on 2021/5/28
 */
interface AladdinViewAgent {

    /**
     * Bind a view to this agent.
     * All other operation should happened after binding.
     */
    fun bind(view: AladdinView)

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
     * return the size of parent view
     */
    fun getParentSize(): Size

    /**
     * get X in parent of this view
     */
    fun getX(): Int

    /**
     * get Y in parent of this view
     */
    fun getY(): Int

    /**
     * show the view
     */
    fun show()

    /**
     * dismiss the view
     */
    fun dismiss()

    /**
     * deactivate the view, remove from super but still in shown state
     */
    fun deactivate()

    /**
     * activate the view.
     * if the view is not been shown, this function does nothing
     */
    fun activate()
}
