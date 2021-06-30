package com.kyleduo.aladdin.genie.okhttp

import okhttp3.OkHttpClient
import java.net.Proxy

/**
 * @author kyleduo on 2021/6/30
 */
object OkHttpHelper {

    private val proxyField by lazy {
        OkHttpClient::class.java.getDeclaredField("proxy").also {
            it.isAccessible = true
        }
    }

    fun forceSetProxy(client: OkHttpClient, proxy: Proxy?) {
        if (client.proxy == proxy) {
            return
        }

        proxyField.set(client, proxy)

        client.connectionPool.evictAll()
    }
}