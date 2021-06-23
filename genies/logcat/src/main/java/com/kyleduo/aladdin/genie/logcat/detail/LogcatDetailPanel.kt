package com.kyleduo.aladdin.genie.logcat.detail

import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.ViewGroup
import android.widget.TextView
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

        val content: String = item.content.replace("\n", "\n\n")
        val ss: Spannable = SpannableString(content)
        for (i in 1 until content.length - 1) {
            if (content[i] == '\n' && content[i - 1] == '\n') {
                ss.setSpan(
                    RelativeSizeSpan(0.5f), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        binding.aladdinLogcatLogDetailContent.setText(ss, TextView.BufferType.SPANNABLE)

        val itemStyle = logItemStyles.getStyle(item.level)

        fun TextView.applyStyle() {
            setTextColor(itemStyle.textColor)
        }

        binding.aladdinLogcatLogDetailPanelContent.background = itemStyle.detailBackground
        binding.aladdinLogcatLogDetailClose.imageTintList =
            ColorStateList.valueOf(itemStyle.badgeTextColor)
        binding.aladdinLogcatLogDetailClose.background =
            UIUtils.createRoundCornerDrawable(itemStyle.badgeBackgroundColor, 4f.dp2px())
        binding.aladdinLogcatLogDetailLevel.text = item.level.name
        binding.aladdinLogcatLogDetailTime.text = item.time
        binding.aladdinLogcatLogDetailTag.text = item.tag
        binding.aladdinLogcatLogDetailTid.text = item.tid

        binding.aladdinLogcatLogDetailLevel.applyStyle()
        binding.aladdinLogcatLogDetailTime.applyStyle()
        binding.aladdinLogcatLogDetailTid.applyStyle()
        binding.aladdinLogcatLogDetailTag.applyStyle()
        binding.aladdinLogcatLogDetailContent.applyStyle()
    }

    fun dismiss() {
        val root = binding.root
        (root.parent as? ViewGroup)?.removeView(root)
    }
}