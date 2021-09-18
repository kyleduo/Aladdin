package com.kyleduo.aladdin.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author kyleduo on 2021/6/24
 */
class SimpleOffsetDecoration(
    private val horizontal: Int,
    private val vertical: Int,
    private val spacing: Int = horizontal
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = parent.adapter?.itemCount ?: 0
        if (itemCount == 0) {
            return
        }
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val spanIndex = (view.layoutParams as? GridLayoutManager.LayoutParams)?.spanIndex ?: 0
        val spanSize = (view.layoutParams as? GridLayoutManager.LayoutParams)?.spanSize ?: 1
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1

        val isLast = position + 1 == itemCount
        val isLeft = spanIndex == 0
        val isRight = spanIndex == spanCount - 1 || spanIndex + spanSize == spanCount

        val bottom = if (isLast) vertical else 0
        val left = if (isLeft) horizontal else spacing / 2
        val right = if (isRight) horizontal else spacing / 2

        outRect.set(left, vertical, right, bottom)
    }
}