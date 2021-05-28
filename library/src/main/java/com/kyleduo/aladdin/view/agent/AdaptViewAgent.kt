package com.kyleduo.aladdin.view.agent

import android.view.ViewGroup
import android.widget.FrameLayout
import com.kyleduo.aladdin.lifecycle.ActivityLifecycleManager
import com.kyleduo.aladdin.view.IAladdinView

/**
 * View agent in [com.kyleduo.aladdin.view.AladdinViewMode.Adaptive] mode.
 * The view will be added to every [Activity]'s window.
 *
 * @author kyleduo on 2021/5/28
 */
class AdaptViewAgent(
    private val lifecycleManager: ActivityLifecycleManager
) : IAladdinViewAgent {

    private val layoutParams = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    private lateinit var view: IAladdinView

    init {
        TODO("register lifecycle callback to move view through activities")
    }

    override fun bind(view: IAladdinView) {
        this.view = view
    }

    override fun resize(width: Int, height: Int) {
        layoutParams.width = width
        layoutParams.height = height
        TODO("request layout")
    }

    override fun gravity(gravity: Int) {
        layoutParams.gravity = gravity
        TODO("request layout")
    }

    override fun moveTo(x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun moveBy(dx: Int, dy: Int) {
        TODO("Not yet implemented")
    }

    override fun show() {
        TODO("Not yet implemented")
    }

    override fun dismiss() {
        TODO("Not yet implemented")
    }
}
