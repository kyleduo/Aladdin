package com.kyleduo.aladdin.genie.appinfo.providers

import android.content.Context
import android.os.Build
import com.kyleduo.aladdin.genie.info.InfoProvider
import com.kyleduo.aladdin.genie.info.data.InfoItem
import com.kyleduo.aladdin.genie.info.data.InfoSection

/**
 * @author kyleduo on 2021/6/11
 */
class ApplicationInfoProvider(
    private val context: Context
) : InfoProvider {

    @Suppress("DEPRECATION")
    override fun provideAppInfo(): InfoSection {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)

        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode
        }

        return InfoSection(
            "App", "", listOf(
                InfoItem("Package", context.packageName),
                InfoItem("Version Code", versionCode.toString()),
                InfoItem("Version Name", packageInfo.versionName),
                InfoItem("PID", android.os.Process.myPid().toString()),
            )
        )
    }
}
