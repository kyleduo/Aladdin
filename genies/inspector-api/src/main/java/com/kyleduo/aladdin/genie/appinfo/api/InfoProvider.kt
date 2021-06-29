package com.kyleduo.aladdin.genie.appinfo.api

import com.kyleduo.aladdin.genie.appinfo.api.data.InfoSection

/**
 * Providing app info to [InspectorGenie]
 * @author kyleduo on 2021/6/11
 */
interface InfoProvider {

    fun provideAppInfo(): InfoSection?
}
