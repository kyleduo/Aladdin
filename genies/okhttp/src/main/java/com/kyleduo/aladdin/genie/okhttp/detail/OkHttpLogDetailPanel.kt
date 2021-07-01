package com.kyleduo.aladdin.genie.okhttp.detail

import android.view.View
import com.kyleduo.aladdin.api.manager.genie.AladdinPanelController
import com.kyleduo.aladdin.genie.okhttp.R
import com.kyleduo.aladdin.genie.okhttp.data.HttpLog
import com.kyleduo.aladdin.genie.okhttp.databinding.AladdinGenieOkhttpLogDetailPanelBinding
import com.kyleduo.aladdin.ui.FloatingPanel

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
    }

    private var log: HttpLog? = null

    fun show(log: HttpLog) {
        this.log = log
        super.show()
    }

    override fun onShow() {
        super.onShow()
        val log = log ?: return

    }
}