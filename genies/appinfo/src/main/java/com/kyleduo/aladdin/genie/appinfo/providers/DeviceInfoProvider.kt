package com.kyleduo.aladdin.genie.appinfo.providers

import android.content.Context
import android.os.Build
import com.kyleduo.aladdin.genie.info.InfoProvider
import com.kyleduo.aladdin.genie.info.data.InfoItem
import com.kyleduo.aladdin.genie.info.data.InfoSection

/**
 * @author kyleduo on 2021/6/11
 */
class DeviceInfoProvider(
    private val context: Context
) : InfoProvider {

    @Suppress("DEPRECATION")
    override fun provideAppInfo(): InfoSection {
        return InfoSection(
            "Device", "", listOf(
                InfoItem("Brand", Build.BRAND),
                InfoItem("Model", Build.MODEL),
                InfoItem("Manufacturer", Build.MANUFACTURER),
                InfoItem("OS Version", Build.VERSION.RELEASE),
                InfoItem("Android Version", Build.VERSION.SDK_INT.toString()),
            )
        )
    }
}
