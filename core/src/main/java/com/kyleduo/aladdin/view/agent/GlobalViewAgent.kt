package com.kyleduo.aladdin.view.agent

import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import com.kyleduo.aladdin.view.IAladdinView

/**
 * View agent in [com.kyleduo.aladdin.view.AladdinViewMode.Global] mode.
 * The view will be added to the system window. In this situation
 * "android.permission.SYSTEM_ALERT_WINDOW" permission is required.
 *
 * @author kyleduo on 2021/5/28
 */
class GlobalViewAgent(
    private val windowManager: WindowManager
) : IAladdinViewAgent {

    private val layoutParams = WindowManager.LayoutParams().also {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            it.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            it.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
        }
        it.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        it.format = PixelFormat.TRANSLUCENT

        it.gravity = Gravity.START or Gravity.TOP
        it.width = ViewGroup.LayoutParams.WRAP_CONTENT
        it.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }
    private lateinit var view: IAladdinView
    private var isActive = true
    private var isShown = false

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
        layoutParams.x = x
        layoutParams.y = y
        updateLayout()
    }

    override fun moveBy(dx: Int, dy: Int) {
        layoutParams.x += dx
        layoutParams.y += dy
        updateLayout()
    }

    override fun show() {
        isShown = true
        if (isActive) {
            windowManager.addView(view.view, layoutParams)
        }
    }

    override fun dismiss() {
        isShown = false
        windowManager.removeView(view.view)
    }

    private fun updateLayout() {
        if (view.view.parent != null) {
            windowManager.updateViewLayout(view.view, layoutParams)
        }
    }

    override fun deactivate() {
        if (view.view.parent != null) {
            isActive = false
            windowManager.removeView(view.view)
        }
    }

    override fun activate() {
        isActive = true
        if (isShown && view.view.parent == null) {
            windowManager.addView(view.view, layoutParams)
        }
    }
}
