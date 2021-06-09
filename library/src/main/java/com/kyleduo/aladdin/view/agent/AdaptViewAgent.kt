package com.kyleduo.aladdin.view.agent

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kyleduo.aladdin.lifecycle.ActivityLifecycleCallbacksAdapter
import com.kyleduo.aladdin.lifecycle.ActivityLifecycleManager
import com.kyleduo.aladdin.view.IAladdinView
import java.lang.ref.WeakReference

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

        (toActivity.window.decorView as? ViewGroup)?.addView(view.view)
    }

    override fun bind(view: IAladdinView) {
        this.view = view
    }

    override fun resize(width: Int, height: Int) {
        layoutParams.width = width
        layoutParams.height = height

        updateLayout()
    }

    override fun gravity(gravity: Int) {
        layoutParams.gravity = gravity

        updateLayout()
    }

    override fun moveTo(x: Int, y: Int) {
        layoutParams.marginStart = x
        layoutParams.topMargin = y

        updateLayout()
    }

    override fun moveBy(dx: Int, dy: Int) {
        layoutParams.marginStart += dx
        layoutParams.topMargin += dy

        updateLayout()
    }

    override fun show() {
        isShown = true
        if (isActive) {
            currentActivityRef?.get()?.window?.decorView?.let {
                (it as ViewGroup).addView(view.view, layoutParams)
            }
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
            currentActivityRef?.get()?.window?.decorView?.let {
                (it as ViewGroup).addView(view.view, layoutParams)
            }
        }
    }

    private fun updateLayout() {
        if (view.view.parent != null) {
            view.view.requestLayout()
        }
    }
}
