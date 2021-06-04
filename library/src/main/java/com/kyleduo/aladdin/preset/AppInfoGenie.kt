package com.kyleduo.aladdin.preset

import android.view.View
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.genies.ViewGenie
import com.kyleduo.aladdin.utils.app

/**
 * @author kyleduo on 2021/6/4
 */
class AppInfoGenie : ViewGenie() {
    override val panel: View = View(Aladdin.app)
    override val title: String = "App Info"

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
