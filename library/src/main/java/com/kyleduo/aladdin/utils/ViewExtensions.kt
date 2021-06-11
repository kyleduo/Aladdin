package com.kyleduo.aladdin.utils

import com.kyleduo.aladdin.Aladdin

/**
 * @author kyleduo on 2021/5/31
 */

/**
 * dp to px, in int format
 */
fun Int.dp2px(): Int {
    return (Aladdin.app.resources.displayMetrics.density * this).toInt()
}

/**
 * dp to px, in int format
 */
fun Float.dp2px(): Float {
    return Aladdin.app.resources.displayMetrics.density * this
}