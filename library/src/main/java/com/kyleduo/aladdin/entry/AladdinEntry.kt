package com.kyleduo.aladdin.entry

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.utils.app

/**
 * Interface for entry view.
 *
 * @author kyleduo on 2021/5/18
 */
class AladdinEntry {

    private val windowManager by lazy {
        Aladdin.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val view: View by lazy {
        RelativeLayout(Aladdin.app).apply {
            setBackgroundColor(Color.CYAN)
        }
    }

    private val layoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams().also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                it.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
            }
            it.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            it.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            it.width = 100
            it.height = 100
        }
    }

    fun show() {
        if (view.parent == null) {
            addToWindow()
        }
        view.visibility = View.VISIBLE
    }

    private fun addToWindow() {
        windowManager.addView(view, layoutParams)
    }

    fun hide() {
        view.visibility = View.GONE
    }
}
