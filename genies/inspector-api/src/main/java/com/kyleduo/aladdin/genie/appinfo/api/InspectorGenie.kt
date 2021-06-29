package com.kyleduo.aladdin.genie.appinfo.api

import androidx.annotation.MainThread

/**
 * Display basic information of this Application.
 *
 * @author kyleduo on 2021/6/29
 */
interface InspectorGenie {

    @MainThread
    fun registerInfoProvider(provider: InfoProvider)

    @MainThread
    fun unregisterInfoProvider(provider: InfoProvider)
}
