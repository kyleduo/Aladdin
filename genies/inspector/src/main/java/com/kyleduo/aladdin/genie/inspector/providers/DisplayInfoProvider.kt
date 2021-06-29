package com.kyleduo.aladdin.genie.inspector.providers

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.WindowManager
import com.kyleduo.aladdin.genie.inspector.api.InfoProvider
import com.kyleduo.aladdin.genie.inspector.api.data.InfoItem
import com.kyleduo.aladdin.genie.inspector.api.data.InfoSection

/**
 * @author kyleduo on 2021/6/11
 */
class DisplayInfoProvider(
    private val context: Context
) : InfoProvider {

    @Suppress("DEPRECATION")
    override fun provideInfo(): InfoSection {
        val windowManager: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowBounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds
        } else {
            Rect(0, 0, windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
        }
        return InfoSection(
            "Display", "", listOf(
                InfoItem("window size", "(${windowBounds.width()}, ${windowBounds.height()})"),
                InfoItem("density", context.resources.displayMetrics.density.toString()),
                InfoItem("densityDpi", context.resources.displayMetrics.densityDpi.toString()),
            )
        )
    }
}
