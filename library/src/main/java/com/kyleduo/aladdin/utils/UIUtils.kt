package com.kyleduo.aladdin.utils

import android.util.Size
import com.kyleduo.aladdin.Aladdin

/**
 * @author kyleduo on 2021/5/31
 */
object UIUtils {

    val screenSize: Size
        get() {
            return Size(
                Aladdin.app.resources.displayMetrics.widthPixels,
                Aladdin.app.resources.displayMetrics.heightPixels
            )
        }
}