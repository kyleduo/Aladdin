package com.kyleduo.aladdin.app

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author kyleduo on 2021/6/30
 */
object ApiClientFactory {

    private val BASE_URL = if (BuildConfig.HTTPBIN_BASE_URL.isEmpty()) {
        "https://httpbin.org"
    } else {
        BuildConfig.HTTPBIN_BASE_URL
    }

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val retrofit: Retrofit by lazy { createRetrofit(client) }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}