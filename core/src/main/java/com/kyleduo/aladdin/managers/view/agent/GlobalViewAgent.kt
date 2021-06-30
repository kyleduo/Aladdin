package com.kyleduo.aladdin.managers.view.agent

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.view.AladdinView
import com.kyleduo.aladdin.api.manager.view.AladdinViewAgent
import com.kyleduo.aladdin.managers.view.ViewDraggingHelper
import com.kyleduo.aladdin.managers.view.ViewPositionStorage


/**
 * View agent in [com.kyleduo.aladdin.api.manager.view.AladdinViewMode.Global] mode.
 * The view will be added to the system window. In this situation
 * "android.permission.SYSTEM_ALERT_WINDOW" permission is required.
 *
 * @author kyleduo on 2021/5/28
 */
class GlobalViewAgent(
    val context: AladdinContext,
    val viewPositionStorage: ViewPositionStorage
) : AladdinViewAgent, ViewDraggingHelper.OnViewSnappedListener, View.OnAttachStateChangeListener {
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
    private val viewDraggingHelper by lazy {
        ViewDraggingHelper(
            context,
            this.view,
            this
        )
    }

    override var isSoftInputEnabled: Boolean = false
        set(value) {
            field = value

            if (value) {
                // DON'T known why FLAG_ALT_FOCUSABLE_IM is not working here,
                // So it's needed to toggle the FLAG_NOT_FOCUSABLE flag.
                layoutParams.flags =
                    layoutParams.flags and (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE).inv()
            } else {
                layoutParams.flags =
                    layoutParams.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            }
            updateLayout()
        }

    override fun bind(view: AladdinView) {
        this.view = view

        if (this.view.isDraggable) {
            this.view.view.setOnTouchListener(viewDraggingHelper)
            this.view.view.addOnAttachStateChangeListener(this)
            viewPositionStorage.get(this.view)?.let {
                moveTo(it.x, it.y)
            }
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

    override fun getParentSize(): Size {
        val windowBounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Rect().also { rect ->
                val metrics = windowManager.currentWindowMetrics
                val cutout = metrics.windowInsets.displayCutout
                rect.set(metrics.bounds)
                cutout?.let {
                    rect.left += cutout.safeInsetLeft
                    rect.top += cutout.safeInsetTop
                    rect.right -= cutout.safeInsetRight
                    rect.bottom -= cutout.safeInsetBottom
                }
            }
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
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
            if (view.view.parent != null) {
                windowManager.removeView(view.view)
            }
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
        viewPositionStorage.save(view)
    }

    override fun onViewAttachedToWindow(v: View?) {
        viewDraggingHelper.trySnapToEdge()
    }

    override fun onViewDetachedFromWindow(v: View?) {
    }
}
