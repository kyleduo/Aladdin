package com.kyleduo.aladdin.genie.okhttp

import com.kyleduo.aladdin.genie.okhttp.data.HttpLog

/**
 * @author kyleduo on 2021/7/1
 */
interface OnInterceptRequestListener {

    fun onRequestStarted(log: HttpLog)

    fun onRequestFinished(log: HttpLog)
}