package com.kyleduo.aladdin.genie.okhttp.data

import java.net.URL

/**
 * @author kyleduo on 2021/7/1
 */
data class HttpLog(
    val id: Int,
    val startTime: Long,
    val request: RequestLog,
    val response: ResponseLog? = null,
    val durationInMs: Long = 0L,
)

data class RequestLog(
    val url: URL,
    val method: String,
    val headers: List<Pair<String, String>>,
    val body: String,
)

data class ResponseLog(
    val statusCode: Int = 0,
    val headers: List<Pair<String, String>>? = null,
    val body: String? = null,
    val bodyLength: Long? = 0,
    val errorMessage: String? = null,
)