package com.kyleduo.aladdin.genie.okhttp.api

import okhttp3.OkHttpClient

/**
 * Provider used to provide OkHttpClient to OkHttpGenie.
 *
 * @author kyleduo on 2021/6/30
 */
interface OkHttpClientProvider {

    fun provideClient(): OkHttpClient?
}
