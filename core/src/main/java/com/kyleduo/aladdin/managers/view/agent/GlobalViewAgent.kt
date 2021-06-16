package com.kyleduo.aladdin.managers.view.agent

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.util.Size
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.view.AladdinView
import com.kyleduo.aladdin.api.manager.view.AladdinViewAgent
import com.kyleduo.aladdin.managers.view.ViewDraggingHelper

/**
 * View agent in [com.kyleduo.aladdin.api.manager.view.ViewMode.Global] mode.
 * The view will be added to the system window. In this situation
 * "android.permission.SYSTEM_ALERT_WINDOW" permission is required.
 *
 * @author kyleduo on 2021/5/28
 */
class GlobalViewAgent(
    val context: AladdinContext
) : AladdinViewAgent, ViewDraggingHelper.OnViewSnappedListener {
    private val windowManager: WindowManager =
        context.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    @Suppress("DEPRECATION")
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
    private lateinit var view: AladdinView
    private var isActive = true
    private var isShown = false

    override fun bind(view: AladdinView) {
        this.view = view

        if (this.view.isDraggable) {
            this.view.view.setOnTouchListener(
                ViewDraggingHelper(
                    context,
                    this,
                    this.view.autoSnapEdges,
                    this
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
        layoutParams.x = x
        layoutParams.y = y
        updateLayout()
    }

    override fun moveBy(dx: Int, dy: Int) {
        layoutParams.x += dx
        layoutParams.y += dy
        updateLayout()
    }

    @Suppress("DEPRECATION")
    override fun getParentSize(): Size {
        // FIXME: 2021/6/15 kyleduo find a way to get exact size of ViewRootImpl
        val windowBounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds
        } else {
            Rect(0, 0, windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
        }
        return Size(windowBounds.width(), windowBounds.height())
    }

    override fun getX(): Int {
        return layoutParams.x
    }

    override fun getY(): Int {
        return layoutParams.y
    }

    override fun show() {
        isShown = true
        if (isActive) {
            windowManager.addView(view.view, layoutParams)
            windowManager
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

    override fun onViewSnapped() {
        // TODO: 2021/6/16 kyleduo persist view's position
    }
}
