package com.kyleduo.aladdin.preset

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.genies.ViewGenie

/**
 * @author kyleduo on 2021/6/4
 */
class AppInfoGenie : ViewGenie() {
    override val title: String = "App Info"

    override fun createPanel(container: ViewGroup): View {
        return View(container.context).apply {
            setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun onSelected() {
    }

    override fun onDeselected() {
    }

    override val key: String = this.javaClass.name

    override fun onStart() {
    }

    override fun onStop() {
    }
}
