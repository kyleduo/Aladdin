package com.kyleduo.aladdin.genie.appinfo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.genies.ViewGenie

/**
 * Display basic information of this Application.
 *
 * @author kyleduo on 2021/6/11
 */
class AppInfoGenie : ViewGenie() {
    companion object {
        const val KEY = "aladdin-app-info"
    }

    override val title: String = "App Info"
    override val key: String = KEY

    override fun createPanel(container: ViewGroup): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.aladdin_genie_app_info_panel, container, false).also {
                it.setBackgroundColor(Color.LTGRAY)
            }
    }

    override fun onSelected() {
    }

    override fun onDeselected() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }
}
