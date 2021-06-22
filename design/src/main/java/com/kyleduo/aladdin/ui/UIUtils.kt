package com.kyleduo.aladdin.ui

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

/**
 * Utility methods for building user interface.
 *
 * @author kyleduo on 2021/6/23
 */
object UIUtils {

    fun createRoundCornerDrawable(solidColor: Int, cornerRadius: Float): Drawable {
        return createRoundCornerDrawable(
            solidColor,
            arrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        )
    }

    fun createRoundCornerDrawable(solidColor: Int, cornerRadius: Array<Float>): Drawable {
        val topLeft = cornerRadius[0]
        val topRight = cornerRadius[1]
        val bottomRight = cornerRadius[2]
        val bottomLeft = cornerRadius[3]
        return GradientDrawable().also {
            it.shape = GradientDrawable.RECTANGLE
            it.cornerRadii = floatArrayOf(
                topLeft, topLeft,
                topRight, topRight,
                bottomRight, bottomRight,
                bottomLeft, bottomLeft
            )
            it.setColor(solidColor)
        }
    }
}