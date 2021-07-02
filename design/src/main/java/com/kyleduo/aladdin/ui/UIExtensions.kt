package com.kyleduo.aladdin.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
    return Aladdin.context?.let {
        (it.app.resources.displayMetrics.density * this).toInt()
    } ?: 0
}

/**
 * dp to px, in float format
 */
fun Float.dp2px(): Float {
    return Aladdin.context?.let {
        it.app.resources.displayMetrics.density * this
    } ?: 0f
}

/**
 * dp to px, in int format
 */
fun View.dp2px(dp: Int): Int {
    return (resources.displayMetrics.density * dp).toInt()
}

/**
 * dp to px, in float format
 */
fun View.dp2px(dp: Float): Float {
    return resources.displayMetrics.density * dp
}

/**
 * Make the TextView support copy content to clipboard when long clicked.
 */
fun <T : View> T.supportCopy(textProvider: (T) -> CharSequence) {
    setOnLongClickListener {
        val clipboardManager =
            it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Simple", textProvider(this)))
        Toast.makeText(it.context, "Copied!", Toast.LENGTH_SHORT).show()
        true
    }
}

/**
 * Make the TextView support copy content to clipboard when long clicked.
 */
fun <T : TextView> T.supportCopy() {
    setOnLongClickListener {
        val clipboardManager =
            it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Simple", this.text))
        Toast.makeText(it.context, "Copied!", Toast.LENGTH_SHORT).show()
        true
    }
}