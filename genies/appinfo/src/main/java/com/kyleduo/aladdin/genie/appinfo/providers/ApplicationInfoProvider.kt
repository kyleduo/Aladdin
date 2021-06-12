package com.kyleduo.aladdin.genie.appinfo.providers

import android.content.Context
import android.os.Build
import com.kyleduo.aladdin.genie.appinfo.AppInfoProvider
import com.kyleduo.aladdin.genie.appinfo.data.AppInfoItem
import com.kyleduo.aladdin.genie.appinfo.data.AppInfoSection

/**
 * @author kyleduo on 2021/6/11
 */
class ApplicationInfoProvider(
    private val context: Context
) : AppInfoProvider {

    @Suppress("DEPRECATION")
    override fun provideAppInfo(): AppInfoSection {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)

        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode
        }

        return AppInfoSection(
            "App", "", listOf(
                AppInfoItem("Package", context.packageName),
                AppInfoItem("Version Code", versionCode.toString()),
                AppInfoItem("Version Name", packageInfo.versionName),
                AppInfoItem("PID", android.os.Process.myPid().toString()),
            )
        )
    }
}
