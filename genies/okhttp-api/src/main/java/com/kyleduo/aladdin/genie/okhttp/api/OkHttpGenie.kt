package com.kyleduo.aladdin.genie.okhttp.api

/**
 * Used to inspect the situation of okhttp clients.
 *
 * @author kyleduo on 2021/6/30
 */
interface OkHttpGenie {

    /**
     * Register a provider
     */
    fun registerOkHttpClientProvider(provider: OkHttpClientProvider)

    /**
     * Unregister a provider. You will want to unregister a provider when you want to release
     * an okhttp client.
     */
    fun unregisterOkHttpClientProvider(provider: OkHttpClientProvider)
}
