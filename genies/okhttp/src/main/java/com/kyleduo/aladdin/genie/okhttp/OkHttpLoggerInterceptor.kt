package com.kyleduo.aladdin.genie.okhttp

import android.os.Handler
import android.os.Looper
import com.kyleduo.aladdin.genie.okhttp.data.HttpLog
import com.kyleduo.aladdin.genie.okhttp.data.RequestLog
import com.kyleduo.aladdin.genie.okhttp.data.ResponseLog
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Inspired by okhttp3.logging.HttpLoggingInterceptor (@square/okhttp)
 *
 * @author kyleduo on 2021/7/1
 */
class OkHttpLoggerInterceptor(
    _isLogEnabled: Boolean = false,
    private val listener: OnInterceptRequestListener
) : Interceptor {

    private val mainHandler = Handler(Looper.getMainLooper())
    var isLogEnabled: Boolean = _isLogEnabled
        set(value) {
            field = value
            isLogEnabledAtomic.set(value)
        }
    private val isLogEnabledAtomic = AtomicBoolean(_isLogEnabled)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!isLogEnabledAtomic.get()) {
            return chain.proceed(request)
        }

        val url = request.url.toUrl()
        val requestHeaders = parseHeaders(request.headers)

        request.body?.let { requestBody ->
            requestBody.contentType()?.let { mediaType ->
                if (requestHeaders.find { it.first == "Content-Type" } == null) {
                    requestHeaders.add(Pair("Content-Type", mediaType.toString()))
                }
            }

            if (requestBody.contentLength() != -1L) {
                if (requestHeaders.find { it.first == "Content-Length" } == null) {
                    requestHeaders.add(
                        Pair(
                            "Content-Length",
                            requestBody.contentLength().toString()
                        )
                    )
                }
            }
        }

        val requestBodyValue = parseRequestBody(request)

        val requestLog = RequestLog(
            url,
            request.method,
            requestHeaders,
            requestBodyValue
        )

        val log = HttpLog(
            request.hashCode(),
            System.currentTimeMillis(),
            requestLog
        )
        runOnMainThread { listener.onRequestStarted(log) }

        val startTimeInNs = System.nanoTime()
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            if (isLogEnabledAtomic.get()) {
                val errorMessage = "[${e.javaClass.name}] ${e.message}"
                val errorLog = log.copy(response = ResponseLog(errorMessage = errorMessage))
                runOnMainThread { listener.onRequestFinished(errorLog) }
            }
            throw e
        }

        if (!isLogEnabledAtomic.get()) {
            return response
        }
        val durationInMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTimeInNs)
        val responseHeaders = parseHeaders(response.headers)
        val responseBodyStr = parseResponseBody(response)
        val responseLog = ResponseLog(
            response.code,
            responseHeaders,
            responseBodyStr,
            response.body?.contentLength()
        )
        val finishLog = log.copy(response = responseLog, durationInMs = durationInMs)
        runOnMainThread { listener.onRequestFinished(finishLog) }

        return response
    }

    private fun runOnMainThread(block: () -> Unit) {
        mainHandler.post(block)
    }

    private fun parseRequestBody(request: Request): String {
        val requestBody = request.body
        return when {
            requestBody == null -> "[NO BODY]"
            requestBody.isDuplex() -> "[DUPLEX]"
            requestBody.isOneShot() -> "[ONE-SHOT]"
            bodyHasUnknownEncoding(request.headers) -> "[UNKNOWN-ENCODING]"
            else -> {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
                if (buffer.isProbablyUtf8()) {
                    buffer.readString(charset)
                } else {
                    "[BINARY ${requestBody.contentLength()} bytes]"
                }
            }
        }
    }

    private fun parseResponseBody(response: Response): String {
        val responseBody = response.body
        return when {
            responseBody == null || !response.promisesBody() -> "[NO BODY]"
            bodyHasUnknownEncoding(response.headers) -> "[UNKNOWN-ENCODING]"
            responseBody.contentLength() > 1024 * 1024 -> "[TOO-LARGE ${responseBody.contentLength()} bytes]"
            else -> {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer
                if ("gzip".equals(response.headers["Content-Encoding"], ignoreCase = true)) {
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val contentType = responseBody.contentType()
                val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                if (!buffer.isProbablyUtf8()) {
                    "[BINARY ${buffer.size} bytes]"
                } else if (responseBody.contentLength() != 0L) {
                    buffer.clone().readString(charset)
                } else {
                    "[${buffer.size} bytes]"
                }
            }
        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small
     * sample of code points to detect unicode control characters commonly used in binary file
     * signatures.
     */
    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size.coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun parseHeaders(headers: Headers): MutableList<Pair<String, String>> {
        val headerList = mutableListOf<Pair<String, String>>()
        for (i in 0 until headers.size) {
            val name = headers.name(i)
            val value = headers.value(i)
            headerList.add(Pair(name, value))
        }
        return headerList
    }
}