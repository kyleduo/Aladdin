package com.kyleduo.aladdin.managers.view.agent

import android.app.Activity
import android.util.Size
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.lifecycle.ActivityLifecycleCallbacksAdapter
import com.kyleduo.aladdin.api.manager.lifecycle.LifecycleManager
import com.kyleduo.aladdin.api.manager.view.AladdinView
import com.kyleduo.aladdin.api.manager.view.AladdinViewAgent
import com.kyleduo.aladdin.managers.view.ViewDraggingHelper
import java.lang.ref.WeakReference

/**
 * View agent in [com.kyleduo.aladdin.api.manager.view.ViewMode.Adaptive] mode.
 * The view will be added to every [Activity]'s window.
 *
 * @author kyleduo on 2021/5/28
 */
class AdaptViewAgent(
    val context: AladdinContext
) : AladdinViewAgent {

    private val lifecycleManager: LifecycleManager = context.lifecycleManager

    private val layoutParams = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    private lateinit var view: AladdinView
    private var isActive = true
    private var isShown = false

    private var currentActivityRef: WeakReference<Activity>? = null

    init {
        lifecycleManager.registerActivityLifecycleCallbacks(object :
            ActivityLifecycleCallbacksAdapter() {

            override fun onActivityStarted(activity: Activity) {
                super.onActivityStarted(activity)

                if (isShown) {
                    moveView(activity)
                }
                currentActivityRef = WeakReference(activity)
            }
        })
    }

    private fun moveView(toActivity: Activity) {
        if (view.view.parent != null) {
            (view.view.parent as? ViewGroup)?.removeView(view.view)
        }

        toActivity.addAladdinView(view)
    }

    override fun bind(view: AladdinView) {
        this.view = view

        if (this.view.isDraggable) {
            this.view.view.setOnTouchListener(
                ViewDraggingHelper(
                    context,
                    this,
                    this.view.autoSnapEdges
                )
            )
        }
    }

    override fun resize(width: Int, height: Int) {
        layoutParams.width = width
        layoutParams.height = height

        updateLayout()
    }

    override fun gravity(gravity: Int) {
        if (view.isDraggable) {
            return
        }
        layoutParams.gravity = gravity

        updateLayout()
    }

    override fun moveTo(x: Int, y: Int) {
        layoutParams.leftMargin = x
        layoutParams.topMargin = y

        updateLayout()
    }

    override fun moveBy(dx: Int, dy: Int) {
        layoutParams.leftMargin += dx
        layoutParams.topMargin += dy

        updateLayout()
    }

    override fun getParentSize(): Size {
        return this.currentActivityRef?.get()?.window?.decorView?.let {
            Size(it.width, it.height)
        } ?: Size(0, 0)
    }

    override fun getX(): Int {
        return layoutParams.marginStart
    }

    override fun getY(): Int {
        return layoutParams.topMargin
    }

    override fun show() {
        isShown = true
        if (isActive) {
            currentActivityRef?.get()?.addAladdinView(view)
        }
    }

    override fun dismiss() {
        isShown = false
        (view.view.parent as? ViewGroup)?.removeView(view.view)
    }

    override fun deactivate() {
        if (view.view.parent != null) {
            isActive = false
            (view.view.parent as? ViewGroup)?.removeView(view.view)
        }
    }

    override fun activate() {
        isActive = true
        if (isShown && view.view.parent == null) {
            currentActivityRef?.get()?.addAladdinView(view)
        }
    }

    private fun Activity.addAladdinView(view: AladdinView) {
        if (view.view.parent == this.window?.decorView) {
            // already added
            return
        }
        if (view.view.parent != null) {
            (view.view.parent as? ViewGroup)?.removeView(view.view)
        }
        (this.window?.decorView as? ViewGroup)?.addView(view.view, layoutParams)
    }

    private fun updateLayout() {
        if (view.view.parent != null) {
            view.view.requestLayout()
        }
    }
}
