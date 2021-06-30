package com.kyleduo.aladdin.genie.okhttp

import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpClientProvider
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpGenie
import com.kyleduo.aladdin.genie.okhttp.databinding.AladdinGenieOkhttpPanelBinding
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/30
 */
class OkHttpGenieImpl : AladdinViewGenie(), OkHttpGenie {
    override val title: String = "OkHttp"
    private val providers = mutableSetOf<OkHttpClientProvider>()
    private lateinit var binding: AladdinGenieOkhttpPanelBinding

    override fun createPanel(container: ViewGroup): View {
        return container.inflateView(R.layout.aladdin_genie_okhttp_panel).also {
            binding = AladdinGenieOkhttpPanelBinding.bind(it)
        }
    }

    override fun onSelected() {
    }

    override fun onDeselected() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun registerOkHttpClientProvider(provider: OkHttpClientProvider) {
        providers.add(provider)
    }

    override fun unregisterOkHttpClientProvider(provider: OkHttpClientProvider) {
        providers.remove(provider)
    }
}
