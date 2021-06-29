package com.kyleduo.aladdin.app

import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.api.manager.view.AladdinViewMode
import com.kyleduo.aladdin.genie.appinfo.AppInfoGenie
import com.kyleduo.aladdin.genie.hook.HookGenie
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
                it.addGenie(AppInfoGenie())
                    .addGenie(LogcatGenie())
                    .addGenie(HookGenie())
            }
            .install()
    }
}
