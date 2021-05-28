package com.kyleduo.aladdin.view

import android.content.Context
import android.view.WindowManager
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.structure.IAladdinManager
import com.kyleduo.aladdin.utils.PermissionUtils
import com.kyleduo.aladdin.utils.app
import com.kyleduo.aladdin.view.agent.AdaptViewAgent
import com.kyleduo.aladdin.view.agent.GlobalViewAgent
import com.kyleduo.aladdin.view.agent.IAladdinViewAgent

/**
 * A manager for organizing [IAladdinView]'s.
 *
 * @author kyleduo on 2021/5/25
 */
class AladdinViewManager : IAladdinManager {

    var mode: AladdinViewMode = AladdinViewMode.Global
    private val views = mutableMapOf<Any, IAladdinView>()

    fun register(view: IAladdinView) {
        views[view.tag] = view
        view.bindAgent(createViewAgent())
    }

    override fun attach() {
    }

    override fun ready() {
        if (mode == AladdinViewMode.Global) {
            if (!PermissionUtils.hasAlertWindowPermission()) {
                PermissionUtils.requestAlertWindowPermission()
            }
        }
    }

    private fun createViewAgent(): IAladdinViewAgent {
        return if (mode == AladdinViewMode.Global) {
            val windowManager =
                Aladdin.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            GlobalViewAgent(windowManager)
        } else {
            AdaptViewAgent(Aladdin.context.activityLifecycleManager)
        }
    }
}
