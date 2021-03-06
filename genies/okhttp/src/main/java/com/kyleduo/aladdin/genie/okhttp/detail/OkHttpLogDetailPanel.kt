package com.kyleduo.aladdin.genie.okhttp.detail

import android.view.View
import androidx.core.view.isVisible
import com.kyleduo.aladdin.api.manager.genie.AladdinPanelController
import com.kyleduo.aladdin.genie.okhttp.R
import com.kyleduo.aladdin.genie.okhttp.data.HttpLog
import com.kyleduo.aladdin.genie.okhttp.databinding.AladdinGenieOkhttpLogDetailPanelBinding
import com.kyleduo.aladdin.genie.okhttp.utils.HttpLogFormatter
import com.kyleduo.aladdin.ui.FloatingPanel
import com.kyleduo.aladdin.ui.UIUtils
import com.kyleduo.aladdin.ui.dp2px
import com.kyleduo.aladdin.ui.supportCopy

/**
 * @author kyleduo on 2021/7/2
 */
class OkHttpLogDetailPanel(panelController: AladdinPanelController) :
    FloatingPanel(panelController) {
    override val contentLayoutResId: Int = R.layout.aladdin_genie_okhttp_log_detail_panel
    private lateinit var binding: AladdinGenieOkhttpLogDetailPanelBinding

    override fun onContentInflated(content: View) {
        super.onContentInflated(content)
        binding = AladdinGenieOkhttpLogDetailPanelBinding.bind(content)

        binding.aladdinGenieOkhttpLogDetailUrl.supportCopy()
        binding.aladdinGenieOkhttpLogDetailRequestHeaders.supportCopy()
        binding.aladdinGenieOkhttpLogDetailRequestBody.supportCopy()
        binding.aladdinGenieOkhttpLogDetailResponseHeaders.supportCopy()
        binding.aladdinGenieOkhttpLogDetailResponseBody.supportCopy()
    }

    private var log: HttpLog? = null

    fun show(log: HttpLog) {
        this.log = log
        super.show()
    }

    fun updateLogIfNeed(log: HttpLog) {
        if (!isShowing) {
            return
        }
        if (this.log?.id != log.id) {
            return
        }
        this.log = log
        updateContent()
    }

    override fun onShow() {
        super.onShow()
        updateContent()
    }

    private fun updateContent() {
        val log = log ?: return

        val hasResponse = log.response != null
        val success = log.response?.errorMessage == null
        binding.aladdinGenieOkhttpLogDetailResponseTitle.isVisible = hasResponse
        binding.aladdinGenieOkhttpLogDetailResponseHeadersTitle.isVisible = hasResponse && success
        binding.aladdinGenieOkhttpLogDetailResponseHeaders.isVisible = hasResponse && success
        binding.aladdinGenieOkhttpLogDetailResponseBodyTitle.isVisible = hasResponse && success
        binding.aladdinGenieOkhttpLogDetailResponseBody.isVisible = hasResponse

        log.response?.let {
            if (it.errorMessage != null) {
                binding.aladdinGenieOkhttpLogDetailResponseBody.text = it.errorMessage
            } else {
                binding.aladdinGenieOkhttpLogDetailResponseHeaders.text =
                    it.headers?.let { headers ->
                        HttpLogFormatter.formatHeaders(
                            binding.root.context,
                            headers
                        )
                    }
                binding.aladdinGenieOkhttpLogDetailResponseBody.text = it.body.toString()
            }
        }

        val statusColor = when {
            log.response == null -> 0xFF3B84DB.toInt()
            log.response.statusCode / 100 == 2 -> 0xFF21B818.toInt()
            else -> 0xFFE75656.toInt()
        }
        binding.aladdinGenieOkhttpLogDetailStatus.background =
            UIUtils.createRoundCornerDrawable(statusColor, binding.root.dp2px(4f))

        binding.aladdinGenieOkhttpLogDetailStatus.text =
            log.response?.statusCode?.toString() ?: "..."
        binding.aladdinGenieOkhttpLogDetailMethod.text = log.request.method
        binding.aladdinGenieOkhttpLogDetailStart.text =
            HttpLogFormatter.formatStartTime(binding.root.context, log.startTime)
        binding.aladdinGenieOkhttpLogDetailDuration.text =
            HttpLogFormatter.formatDuration(binding.root.context, log.durationInMs)
        binding.aladdinGenieOkhttpLogDetailUrl.text =
            HttpLogFormatter.formatUrl(binding.root.context, log.request.url)
        binding.aladdinGenieOkhttpLogDetailRequestHeaders.text =
            HttpLogFormatter.formatHeaders(binding.root.context, log.request.headers)
        binding.aladdinGenieOkhttpLogDetailRequestBody.text = log.request.body
    }
}