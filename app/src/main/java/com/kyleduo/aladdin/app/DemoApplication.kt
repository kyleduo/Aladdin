package com.kyleduo.aladdin.app

import android.app.Application
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.AladdinContextFactoryImpl
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.api.manager.view.ViewMode
import com.kyleduo.aladdin.genie.appinfo.AppInfoGenie

/**
 * @author kyleduo on 2021/5/18
 */
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Aladdin.with(this)
            .view()
            .viewMode(ViewMode.Global)
            .end()
            .genie()
            .addGenie { context -> AppInfoGenie(context) }
            .addGenie { context -> TestViewGenie(Color.YELLOW, context) }
            .addGenie { context -> TestViewGenie(Color.CYAN, context) }
            .end()
            .contextFactory(AladdinContextFactoryImpl())
            .install()
    }

    class TestViewGenie(private val color: Int, context: AladdinContext) :
        AladdinViewGenie(context) {
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
