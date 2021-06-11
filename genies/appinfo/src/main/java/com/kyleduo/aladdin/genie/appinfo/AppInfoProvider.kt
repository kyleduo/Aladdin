package com.kyleduo.aladdin.genie.appinfo

import com.kyleduo.aladdin.genie.appinfo.data.AppInfoSection

/**
 * Providing app info to [AppInfoGenie]
 * @author kyleduo on 2021/6/11
 */
interface AppInfoProvider {

    fun provideAppInfo(): AppInfoSection?
}
