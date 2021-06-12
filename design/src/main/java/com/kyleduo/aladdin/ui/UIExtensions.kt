package com.kyleduo.aladdin.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.api.Aladdin

/**
 * get layout inflater from a context instance
 */
fun Context.layoutInflater(): LayoutInflater = LayoutInflater.from(this)

/**
 * inflate a view for this ViewGroup
 */
fun ViewGroup.inflateView(layoutRes: Int): View =
    context.layoutInflater().inflate(layoutRes, this, false)

/**
 * dp to px, in int format
 */
fun Int.dp2px(): Int {
    return (Aladdin.context.app.resources.displayMetrics.density * this).toInt()
}

/**
 * dp to px, in int format
 */
fun Float.dp2px(): Float {
    return Aladdin.context.app.resources.displayMetrics.density * this
}