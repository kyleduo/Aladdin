package com.kyleduo.aladdin.genie.okhttp

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpClientProvider
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpGenie
import com.kyleduo.aladdin.genie.okhttp.data.HttpLog
import com.kyleduo.aladdin.genie.okhttp.databinding.AladdinGenieOkhttpPanelBinding
import com.kyleduo.aladdin.genie.okhttp.detail.OkHttpLogDetailPanel
import com.kyleduo.aladdin.genie.okhttp.view.HttpLogItemViewDelegate
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.SimpleTextInputFloatingPanel
import com.kyleduo.aladdin.ui.inflateView
import okhttp3.OkHttpClient
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*

/**
 * @author kyleduo on 2021/6/30
 */
class OkHttpGenieImpl(title: String = "OkHttp") : AladdinViewGenie(title), OkHttpGenie {

    companion object {
        private const val TAG = "OkHttpGenieImpl"
        private const val KEY_PROXY_HOST = "aladdin_okhttp_proxy_host"

        private val proxyHostRegex = Regex("^(((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.?){4}:\\d+$")
    }

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
            initClients()
        }

    /**
     * Whether log is enabled
     */
    private var isLogEnabled = false
        set(value) {
            field = value

            if (value) {
                initClients()
            }
            refreshIsLogEnabled(value)
        }

    private val proxyHostPanel by lazy {
        SimpleTextInputFloatingPanel(
            panelController,
            object : SimpleTextInputFloatingPanel.OnTextInputConfirmedListener {
                override fun onTextInputConfirmed(text: String) {
                    this@OkHttpGenieImpl.onTextInputConfirmed(text)
                }
            })
    }

    private val onInterceptRequestListener = object : OnInterceptRequestListener {
        override fun onRequestStarted(log: HttpLog) {
            if (!isLogEnabled) {
                return
            }
            logs.add(0, log)
            adapter.notifyItemInserted(0)
        }

        override fun onRequestFinished(log: HttpLog) {
            if (!isLogEnabled) {
                return
            }
            val index = logs.indexOfFirst { it.id == log.id }
            if (index >= 0) {
                logs[index] = log
                adapter.notifyItemChanged(index)
            }
            detailPanel.updateLogIfNeed(log)
        }
    }

    private val logs = mutableListOf<HttpLog>()
    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.items = logs
            it.register(HttpLog::class.java, HttpLogItemViewDelegate(onItemClickListener))
        }
    }

    private val onItemClickListener by lazy {
        object : OnItemClickListener<HttpLog> {
            override fun onItemClick(position: Int, item: HttpLog) {
                onLogItemClicked(item)
            }
        }
    }

    private val detailPanel by lazy {
        OkHttpLogDetailPanel(panelController)
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
            binding.aladdinGenieOkhttpLoggingSwitch.setCheckedImmediately(isLogEnabled)
            binding.aladdinGenieOkhttpProxySwitch.isEnabled = proxy != null
            binding.aladdinGenieOkhttpLoggingList.apply {
                adapter = this@OkHttpGenieImpl.adapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                itemAnimator = null
            }
        }
    }

    override fun onSelected() {
        initClients()
        binding.aladdinGenieOkhttpProxySwitch.setCheckedImmediately(isProxyEnabled)
    }

    override fun registerOkHttpClientProvider(provider: OkHttpClientProvider) {
        providers.add(provider)

        initClients()
    }

    override fun unregisterOkHttpClientProvider(provider: OkHttpClientProvider) {
        providers.remove(provider)
        providerClients.remove(provider)

        initClients()
    }

    private fun initClients() {
        val clients = providers.mapNotNull {
            providerClients[it] ?: it.provideClient().also { client ->
                providerClients[it] = client
            }
        }.toList()

        if (clients.isEmpty()) {
            return
        }

        val realProxy = if (isProxyEnabled) proxy else null

        clients.forEach { client ->
            OkHttpHelper.forceSetProxy(client, realProxy)
            OkHttpHelper.forceAddInterceptor(
                client,
                isLogEnabled,
                onInterceptRequestListener
            )
        }
    }

    private fun refreshIsLogEnabled(isLogEnabled: Boolean) {
        val clients = providers.mapNotNull {
            providerClients[it] ?: it.provideClient().also { client ->
                providerClients[it] = client
            }
        }.toList()

        if (clients.isEmpty()) {
            return
        }

        clients.forEach {
            OkHttpHelper.setLogEnabled(it, isLogEnabled)
        }
    }

    private fun onTextInputConfirmed(text: String) {
        if (text.isEmpty()) {
            return
        }

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

        initClients()
    }

    private fun Proxy.getConfig(): String {
        return (address() as? InetSocketAddress)?.let { address ->
            address.hostName + ":" + address.port
        } ?: ""
    }

    private fun onLogItemClicked(log: HttpLog) {
        detailPanel.show(log)
    }
}
