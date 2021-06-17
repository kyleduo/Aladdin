package com.kyleduo.aladdin.genie.appinfo

import com.kyleduo.aladdin.genie.appinfo.providers.ApplicationInfoProvider
import com.kyleduo.aladdin.genie.appinfo.providers.DeviceInfoProvider
import com.kyleduo.aladdin.genie.appinfo.providers.DisplayInfoProvider
import com.kyleduo.aladdin.genie.info.InfoGenie

/**
 * Display basic information of this Application.
 *
 * @author kyleduo on 2021/6/11
 */
class AppInfoGenie : InfoGenie() {
    companion object {
        const val KEY = "aladdin-app-info"
    }

    override val title: String = "App Info"
    override val key: String = KEY

    override fun onStart() {
        super.onStart()

        registerInfoProvider(ApplicationInfoProvider(context.app))
        registerInfoProvider(DeviceInfoProvider(context.app))
        registerInfoProvider(DisplayInfoProvider(context.app))
    }
}
