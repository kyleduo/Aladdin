package com.kyleduo.aladdin.genie.okhttp

import android.os.Handler
import android.os.Looper
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author kyleduo on 2021/7/1
 */
class OkHttpLoggerInterceptor(
    private val listener: OnInterceptRequestListener
) : Interceptor {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        runOnMainThread { listener.onRequestStarted(request) }

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            runOnMainThread { listener.onRequestFailed(request, e) }
            throw e
        }

        runOnMainThread { listener.onRequestSuccess(request, response) }
        return response
    }

    private fun runOnMainThread(block: () -> Unit) {
        mainHandler.post(block)
    }
}