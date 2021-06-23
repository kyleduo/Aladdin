package com.kyleduo.aladdin.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author kyleduo on 2021/6/24
 */
class SimpleOffsetDecoration(
    val horizontal: Int,
    val vertical: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val isLast = position + 1 == parent.adapter?.itemCount ?: 0

        val bottom = if (isLast) vertical else 0

        outRect.set(horizontal, vertical, horizontal, bottom)
    }
}