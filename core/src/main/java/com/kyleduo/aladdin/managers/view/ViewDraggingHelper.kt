package com.kyleduo.aladdin.managers.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.view.AladdinView
import com.kyleduo.aladdin.api.manager.view.SnapEdge
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Helper class contains implementation of dragging and snapping.
 *
 * @author kyleduo on 2021/6/15
 */
class ViewDraggingHelper(
    val context: AladdinContext,
    val view: AladdinView,
    private val snappedListener: OnViewSnappedListener?
) : View.OnTouchListener {

    private var dragging = false
    private val touchSlot by lazy { ViewConfiguration.get(context.app).scaledTouchSlop }

    private var startX: Float = 0f
    private var startY: Float = 0f
    private var lastX: Float = 0f
    private var lastY: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event ?: return false

        val x = event.rawX
        val y = event.rawY

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
            }
            MotionEvent.ACTION_MOVE -> {
                if (!dragging && (abs(x - startX) > touchSlot || abs(y - startY) > touchSlot)) {
                    dragging = true
                    lastX = startX
                    lastY = startY
                }
                if (dragging) {
                    val dx = x - lastX
                    val dy = y - lastY
                    view.agent.moveBy(dx.toInt(), dy.toInt())
                }
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (dragging) {
                    dragging = false
                    trySnap(v)
                    return true
                }
                dragging = false
            }
        }
        return dragging
    }

    fun trySnapToEdge() {
        trySnap(view.view)
    }

    private fun trySnap(v: View?) {
        v ?: return

        val snapEdges = view.autoSnapEdges
        val agent = view.agent

        if (snapEdges == SnapEdge.NONE) {
            return
        }

        val snapToLeft = snapEdges and SnapEdge.LEFT > 0
        val snapToTop = snapEdges and SnapEdge.TOP > 0
        val snapToRight = snapEdges and SnapEdge.RIGHT > 0
        val snapToBottom = snapEdges and SnapEdge.BOTTOM > 0

        val parentSize = agent.getParentSize()

        val offsetLeft = if (snapToLeft) agent.getX() else Int.MAX_VALUE
        val offsetTop = if (snapToTop) agent.getY() else Int.MAX_VALUE
        val offsetRight =
            if (snapToRight) max(0, parentSize.width - agent.getX() - v.width) else Int.MAX_VALUE
        val offsetBottom =
            if (snapToBottom) max(0, parentSize.height - agent.getY() - v.height) else Int.MAX_VALUE

        val min = min(min(offsetLeft, offsetTop), min(offsetRight, offsetBottom))

        val animator = ValueAnimator().setDuration(200L)
        animator.interpolator = DecelerateInterpolator()

        val xOrY = when (min) {
            offsetLeft -> {
                animator.setIntValues(agent.getX(), 0)
                true
            }
            offsetTop -> {
                animator.setIntValues(agent.getY(), 0)
                false
            }
            offsetRight -> {
                animator.setIntValues(agent.getX(), parentSize.width - v.width)
                true
            }
            offsetBottom -> {
                animator.setIntValues(agent.getY(), parentSize.height - v.height)
                false
            }
            else -> return
        }

        if (xOrY) {
            animator.addUpdateListener {
                agent.moveTo(it.animatedValue as Int, agent.getY())
            }
        } else {
            animator.addUpdateListener {
                agent.moveTo(agent.getX(), it.animatedValue as Int)
            }
        }

        snappedListener?.let {
            animator.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    it.onViewSnapped()
                }
            })
        }
        animator.start()
    }

    /**
     * Listener used to get callback when view has been snapped to edge.
     */
    interface OnViewSnappedListener {

        fun onViewSnapped()
    }
}
