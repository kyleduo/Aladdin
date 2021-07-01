package com.kyleduo.aladdin.genie.okhttp

import okhttp3.OkHttpClient
import java.net.Proxy
import java.util.concurrent.Executors

/**
 * @author kyleduo on 2021/6/30
 */
object OkHttpHelper {

    private val proxyField by lazy {
        OkHttpClient::class.java.getDeclaredField("proxy").also {
            it.isAccessible = true
        }
    }

    private val interceptorsField by lazy {
        OkHttpClient::class.java.getDeclaredField("interceptors").also {
            it.isAccessible = true
        }
    }

    private val executor = Executors.newScheduledThreadPool(0)

    fun forceSetProxy(client: OkHttpClient, proxy: Proxy?) {
        if (client.proxy == proxy) {
            return
        }

        proxyField.set(client, proxy)

        executor.execute {
            client.connectionPool.evictAll()
        }
    }

    fun forceAddInterceptor(
        client: OkHttpClient,
        isLogEnabled: Boolean = false,
        listener: OnInterceptRequestListener
    ) {
        if (client.interceptors.find { it is OkHttpLoggerInterceptor } != null) {
            return
        }

        val newList = client.interceptors.toMutableList().also {
            it.add(OkHttpLoggerInterceptor(isLogEnabled, listener))
        }.toList()

        interceptorsField.set(client, newList)

        executor.execute {
            client.connectionPool.evictAll()
        }
    }

    fun setLogEnabled(client: OkHttpClient, isLogEnabled: Boolean) {
        val found =
            client.interceptors.find { it is OkHttpLoggerInterceptor } as? OkHttpLoggerInterceptor
                ?: return
        found.isLogEnabled = isLogEnabled
    }
}