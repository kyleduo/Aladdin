package com.kyleduo.aladdin.genie.okhttp

import okhttp3.Request
import okhttp3.Response

/**
 * @author kyleduo on 2021/7/1
 */
interface OnInterceptRequestListener {

    fun onRequestStarted(request: Request)

    fun onRequestSuccess(request: Request, response: Response)

    fun onRequestFailed(request: Request, error: Throwable)
}