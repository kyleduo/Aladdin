package com.kyleduo.aladdin.genie.logcat.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import com.kyleduo.aladdin.genie.logcat.data.LogLevel
import com.kyleduo.aladdin.ui.UIUtils
import com.kyleduo.aladdin.ui.dp2px
import kotlin.math.min

/**
 * @author kyleduo on 2021/6/23
 */
class LogItemStyles(val palette: LogItemPalette) {
    private val itemStyles = mutableMapOf<LogLevel, LogItemStyle>()

    fun getStyle(level: LogLevel): LogItemStyle {
        val itemStyle = itemStyles[level]
        if (itemStyle != null) {
            return itemStyle
        }

        val baseColor = palette.getBaseColor(level)
        val hsv = FloatArray(3)
        Color.colorToHSV(baseColor, hsv)

        val backgroundColor =
            Color.HSVToColor(
                floatArrayOf(
                    hsv[0],
                    min(0.2f, hsv[1] * 0.4f),
                    hsv[2] + (1 - hsv[2]) * 0.8f
                )
            )
        val badgeBackgroundColor =
            Color.HSVToColor(
                floatArrayOf(
                    hsv[0],
                    hsv[1] * 0.4f,
                    hsv[2] + (1 - hsv[2]) * 0.5f
                )
            )

        val badgeTextColor =
            Color.HSVToColor(floatArrayOf(hsv[0], hsv[1], hsv[2] * 0.8f))


        val r = 8f.dp2px()

        val badgeBackground = UIUtils.createRoundCornerDrawable(
            badgeBackgroundColor, arrayOf(0f, 0f, r, 0f)
        )

        val detailBackground = UIUtils.createRoundCornerDrawable(backgroundColor, r)

        return LogItemStyle(
            baseColor,
            backgroundColor,
            badgeTextColor,
            baseColor,
            badgeBackgroundColor,
            badgeBackground,
            detailBackground
        ).also { itemStyles[level] = it }
    }
}

data class LogItemStyle(
    val textColor: Int,
    val backgroundColor: Int,
    val badgeTextColor: Int,
    val dividerColor: Int,
    val badgeBackgroundColor: Int,
    val badgeBackground: Drawable,
    val detailBackground: Drawable
)