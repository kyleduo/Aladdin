package com.kyleduo.aladdin.app

import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.api.manager.view.AladdinViewMode
import com.kyleduo.aladdin.genie.actions.ActionsGenie
import com.kyleduo.aladdin.genie.appinfo.InspectorGenieImpl
import com.kyleduo.aladdin.genie.logcat.LogcatGenie

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
                    .addGenie(ActionsGenie())
            }
            .install()
    }
}
