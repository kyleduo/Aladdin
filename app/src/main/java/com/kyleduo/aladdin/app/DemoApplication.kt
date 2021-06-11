package com.kyleduo.aladdin.app

import android.app.Application
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.genie.appinfo.AppInfoGenie
import com.kyleduo.aladdin.genies.ViewGenie

/**
 * @author kyleduo on 2021/5/18
 */
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Aladdin.with(this)
            .addGenie(AppInfoGenie())
            .addGenie(TestViewGenie(Color.RED))
            .addGenie(TestViewGenie(Color.GRAY))
            .install()
    }

    class TestViewGenie(private val color: Int) : ViewGenie() {
        override val title: String
            get() = color.toString()

        override fun createPanel(container: ViewGroup): View {
            return View(container.context).apply {
                setBackgroundColor(color)
            }
        }

        override fun onSelected() {
        }

        override fun onDeselected() {
        }

        override val key: String = color.toString()

        override fun onStart() {
        }

        override fun onStop() {
        }

    }
}
