package com.kyleduo.aladdin.genie.logcat.detail

import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import com.kyleduo.aladdin.api.manager.genie.AladdinPanelController
import com.kyleduo.aladdin.genie.logcat.R
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinGenieLogcatDetailPanelBinding
import com.kyleduo.aladdin.genie.logcat.view.LogItemStyles
import com.kyleduo.aladdin.ui.FloatingPanel
import com.kyleduo.aladdin.ui.UIUtils
import com.kyleduo.aladdin.ui.dp2px


/**
 * @author kyleduo on 2021/6/23
 */
class LogcatDetailPanel(
    panelController: AladdinPanelController,
    private val logItemStyles: LogItemStyles,
) : FloatingPanel(panelController) {

    private lateinit var binding: AladdinGenieLogcatDetailPanelBinding
    override val contentLayoutResId: Int = R.layout.aladdin_genie_logcat_detail_panel

    var item: LogItem? = null

    override fun onContentInflated(content: View) {
        super.onContentInflated(content)
        binding = AladdinGenieLogcatDetailPanelBinding.bind(content)
    }

    override fun onShow() {
        super.onShow()

        val item = this.item ?: return

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

        rootBinding.aladdinDesignFloatingPanelClose.imageTintList =
            ColorStateList.valueOf(itemStyle.badgeTextColor)
        rootBinding.aladdinDesignFloatingPanelClose.background =
            UIUtils.createRoundCornerDrawable(itemStyle.badgeBackgroundColor, 4f.dp2px())

        binding.aladdinLogcatLogDetailPanelContent.background = itemStyle.detailBackground
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
}