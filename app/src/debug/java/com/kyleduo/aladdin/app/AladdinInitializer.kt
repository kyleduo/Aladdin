package com.kyleduo.aladdin.app

import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.api.manager.view.AladdinViewMode
import com.kyleduo.aladdin.genie.actions.ActionsGenieImpl
import com.kyleduo.aladdin.genie.inspector.InspectorGenieImpl
import com.kyleduo.aladdin.genie.logcat.LogcatGenie
import com.kyleduo.aladdin.genie.okhttp.OkHttpGenieImpl

/**
 * @author kyleduo on 2021/6/29
 */
object AladdinInitializer {

    fun initialize(application: DemoApplication) {
        Aladdin.with(application)
            .view {
                it.viewMode(AladdinViewMode.Global)
            }
            .genie {
                it.addGenie(InspectorGenieImpl())
                    .addGenie(LogcatGenie())
                    .addGenie(ActionsGenieImpl())
                    .addGenie(ActionsGenieImpl(title = "Actions #2", key = "a2"))
                    .addGenie(OkHttpGenieImpl())
            }
            .install()
    }
}
