package com.kyleduo.aladdin.genie.appinfo.providers

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.WindowManager
import com.kyleduo.aladdin.genie.appinfo.AppInfoProvider
import com.kyleduo.aladdin.genie.appinfo.data.AppInfoItem
import com.kyleduo.aladdin.genie.appinfo.data.AppInfoSection

/**
 * @author kyleduo on 2021/6/11
 */
class DisplayInfoProvider(
    private val context: Context
) : AppInfoProvider {

    @Suppress("DEPRECATION")
    override fun provideAppInfo(): AppInfoSection {
        val windowManager: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowBounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds
        } else {
            Rect(0, 0, windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
        }
        return AppInfoSection(
            "Display", "", listOf(
                AppInfoItem("window size", "(${windowBounds.width()}, ${windowBounds.height()})"),
                AppInfoItem("density", context.resources.displayMetrics.density.toString()),
                AppInfoItem("densityDpi", context.resources.displayMetrics.densityDpi.toString()),
            )
        )
    }
}
