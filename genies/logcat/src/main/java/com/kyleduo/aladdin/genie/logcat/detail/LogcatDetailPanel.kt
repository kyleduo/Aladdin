package com.kyleduo.aladdin.genie.logcat.detail

import android.content.res.ColorStateList
import android.view.ViewGroup
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinGenieLogcatDetailPanelBinding
import com.kyleduo.aladdin.genie.logcat.view.LogItemStyles
import com.kyleduo.aladdin.ui.UIUtils
import com.kyleduo.aladdin.ui.dp2px
import com.kyleduo.aladdin.ui.layoutInflater

/**
 * @author kyleduo on 2021/6/23
 */
class LogcatDetailPanel(
    private val container: ViewGroup,
    private val logItemStyles: LogItemStyles,
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

        val itemStyle = logItemStyles.getStyle(item.level)

        binding.aladdinLogcatLogDetailPanelContent.background = itemStyle.detailBackground
        binding.aladdinLogcatLogDetailContent.setTextColor(itemStyle.textColor)
        binding.aladdinLogcatLogDetailClose.imageTintList =
            ColorStateList.valueOf(itemStyle.badgeTextColor)
        binding.aladdinLogcatLogDetailClose.background =
            UIUtils.createRoundCornerDrawable(itemStyle.badgeBackgroundColor, 4f.dp2px())
    }

    fun dismiss() {
        val root = binding.root
        (root.parent as? ViewGroup)?.removeView(root)
    }
}