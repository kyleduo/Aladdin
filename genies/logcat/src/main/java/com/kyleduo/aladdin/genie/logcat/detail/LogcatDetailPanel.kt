package com.kyleduo.aladdin.genie.logcat.detail

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinGenieLogcatDetailPanelBinding
import com.kyleduo.aladdin.genie.logcat.view.LogItemPalette
import com.kyleduo.aladdin.ui.dp2px
import com.kyleduo.aladdin.ui.layoutInflater
import kotlin.math.min

/**
 * @author kyleduo on 2021/6/23
 */
class LogcatDetailPanel(
    private val container: ViewGroup,
    private val logItemPalette: LogItemPalette,
) {

    private val binding by lazy {
        AladdinGenieLogcatDetailPanelBinding.inflate(
            container.context.layoutInflater(),
            container,
            false
        ).also {
            it.aladdinLogcatLogDetailClose.setOnClickListener {
                dismiss()
            }
        }
    }

    fun show(item: LogItem) {
        val root = binding.root
        (root.parent as? ViewGroup)?.removeView(root)

        container.addView(root)
        binding.aladdinLogcatLogDetailContent.text = item.content

        val baseColor = logItemPalette.getBaseColor(item.level)
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

        val r = 8f.dp2px()
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.cornerRadii = floatArrayOf(r, r, r, r, r, r, r, r)
        background.setColor(backgroundColor)

        binding.aladdinLogcatLogDetailPanelContent.background = background
        binding.aladdinLogcatLogDetailContent.setTextColor(baseColor)
    }

    fun dismiss() {
        val root = binding.root
        (root.parent as? ViewGroup)?.removeView(root)
    }
}