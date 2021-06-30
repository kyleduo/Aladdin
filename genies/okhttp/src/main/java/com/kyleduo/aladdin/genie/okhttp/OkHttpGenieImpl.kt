package com.kyleduo.aladdin.genie.okhttp

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpClientProvider
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpGenie
import com.kyleduo.aladdin.genie.okhttp.databinding.AladdinGenieOkhttpPanelBinding
import com.kyleduo.aladdin.ui.SimpleTextInputFloatingPanel
import com.kyleduo.aladdin.ui.inflateView
import okhttp3.OkHttpClient
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*

/**
 * @author kyleduo on 2021/6/30
 */
class OkHttpGenieImpl : AladdinViewGenie(), OkHttpGenie {

    companion object {
        private const val TAG = "OkHttpGenieImpl"
        private const val KEY_PROXY_HOST = "aladdin_okhttp_proxy_host"

        private val proxyHostRegex = Regex("^(((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.?){4}:\\d+$")
    }

    override val title: String = "OkHttp"
    override val apiClass: Class<*> = OkHttpGenie::class.java

    private val providers = mutableSetOf<OkHttpClientProvider>()
    private val providerClients = WeakHashMap<OkHttpClientProvider, OkHttpClient>()
    private lateinit var binding: AladdinGenieOkhttpPanelBinding

    /**
     * User configured proxy
     */
    private var proxy: Proxy? = null
        set(value) {
            field = value

            binding.aladdinGenieOkhttpProxyHost.text = value?.getConfig() ?: ""
            binding.aladdinGenieOkhttpProxySwitch.isEnabled = value != null

            if (value == null) {
                binding.aladdinGenieOkhttpProxySwitch.setCheckedImmediately(false)
            }
        }

    /**
     * Whether proxy is enabled
     */
    private var isProxyEnabled = false
        set(value) {
            field = proxy?.let { value } ?: false
            refreshClients()
        }

    /**
     * Whether log is enabled
     */
    private var isLogEnabled = false

    private val proxyHostPanel by lazy {
        SimpleTextInputFloatingPanel(
            panelController,
            object : SimpleTextInputFloatingPanel.OnTextInputConfirmedListener {
                override fun onTextInputConfirmed(text: String) {
                    this@OkHttpGenieImpl.onTextInputConfirmed(text)
                }
            })
    }

    override fun createPanel(container: ViewGroup): View {
        return container.inflateView(R.layout.aladdin_genie_okhttp_panel).also {
            binding = AladdinGenieOkhttpPanelBinding.bind(it)
            binding.aladdinGenieOkhttpProxySwitch.setOnCheckedChangeListener { _, isChecked ->
                isProxyEnabled = isChecked
            }
            binding.aladdinGenieOkhttpLoggingSwitch.setOnCheckedChangeListener { _, isChecked ->
                isLogEnabled = isChecked
            }
            binding.aladdinGenieOkhttpProxyHost.setOnClickListener {
                proxyHostPanel.show(
                    KEY_PROXY_HOST,
                    "Proxy Host [ip:host]",
                    proxy?.getConfig() ?: ""
                )
            }
            binding.aladdinGenieOkhttpProxySwitch.isEnabled = proxy != null
        }
    }

    override fun onSelected() {
        refreshClients()
        binding.aladdinGenieOkhttpProxySwitch.setCheckedImmediately(isProxyEnabled)
    }

    override fun registerOkHttpClientProvider(provider: OkHttpClientProvider) {
        providers.add(provider)

        refreshClients()
    }

    override fun unregisterOkHttpClientProvider(provider: OkHttpClientProvider) {
        providers.remove(provider)
        providerClients.remove(provider)

        refreshClients()
    }

    private fun refreshClients() {
        val clients = providers.mapNotNull {
            providerClients[it] ?: it.provideClient().also { client ->
                providerClients[it] = client
            }
        }.toList()

        if (clients.isEmpty()) {
            return
        }

        val realProxy = if (isProxyEnabled) proxy else null

        clients.forEach {
            OkHttpHelper.forceSetProxy(it, realProxy)
        }
    }

    private fun onTextInputConfirmed(text: String) {
        if (!text.matches(proxyHostRegex)) {
            Log.e(TAG, "Proxy address config is not valid. $text")
            return
        }

        val seg = text.split(":")
        if (seg.size != 2) {
            Log.e(TAG, "Proxy address config is not valid. $text")
            return
        }
        val ip = seg[0]
        val port = seg[1].toIntOrNull()
        if (port == null) {
            Log.e(TAG, "Proxy address config is not valid. $text")
            return
        }

        proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(ip, port))

        refreshClients()
    }

    private fun Proxy.getConfig(): String {
        return (address() as? InetSocketAddress)?.let { address ->
            address.hostName + ":" + address.port
        } ?: ""
    }
}
