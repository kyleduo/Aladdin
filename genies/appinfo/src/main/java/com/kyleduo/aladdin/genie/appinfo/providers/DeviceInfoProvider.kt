package com.kyleduo.aladdin.genie.appinfo.providers

import android.content.Context
import android.os.Build
import com.kyleduo.aladdin.genie.appinfo.AppInfoProvider
import com.kyleduo.aladdin.genie.appinfo.data.AppInfoItem
import com.kyleduo.aladdin.genie.appinfo.data.AppInfoSection

/**
 * @author kyleduo on 2021/6/11
 */
class DeviceInfoProvider(
    private val context: Context
) : AppInfoProvider {

    @Suppress("DEPRECATION")
    override fun provideAppInfo(): AppInfoSection {
        return AppInfoSection(
            "Device", "", listOf(
                AppInfoItem("Brand", Build.BRAND),
                AppInfoItem("Model", Build.MODEL),
                AppInfoItem("Manufacturer", Build.MANUFACTURER),
                AppInfoItem("OS Version", Build.VERSION.RELEASE),
                AppInfoItem("Android Version", Build.VERSION.SDK_INT.toString()),
            )
        )
    }
}
