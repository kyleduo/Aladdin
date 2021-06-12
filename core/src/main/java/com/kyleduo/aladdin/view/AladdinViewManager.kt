package com.kyleduo.aladdin.view

import android.content.Context
import android.view.WindowManager
import android.widget.Toast
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.lifecycle.AladdinAppLifecycleCallbacks
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
class AladdinViewManager : IAladdinManager, AladdinAppLifecycleCallbacks {

    var mode: AladdinViewMode = AladdinViewMode.Adaptive
    private val views = mutableMapOf<Any, AladdinViewEntry>()
    private var requestingPermission = false

    fun register(view: IAladdinView) {
        val entry = AladdinViewEntry(view, createViewAgent(), false)
        views[view.tag] = entry
    }

    override fun attach() {
        Aladdin.context.activityLifecycleManager.registerAppLifecycleCallbacks(this)
    }

    override fun ready() {
        rebindAgents()
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

    private fun requestAlertWindowPermissionIfNeeded() {
        if (mode == AladdinViewMode.Global) {
            if (!PermissionUtils.hasAlertWindowPermission()) {
                Toast.makeText(
                    Aladdin.app,
                    "Need System Alert Window permission in Global mode.",
                    Toast.LENGTH_SHORT
                ).show()
                requestingPermission = true
                PermissionUtils.requestAlertWindowPermission()
            }
        }
    }

    private fun rebindAgents() {
        if (mode == AladdinViewMode.Adaptive || PermissionUtils.hasAlertWindowPermission()) {
            views.forEach {
                if (!it.value.hasAgentBound) {
                    it.value.view.bindAgent(it.value.agent)
                    it.value.hasAgentBound = true
                }
            }
        }
    }

    override fun onAppEnterBackground() {
        views.forEach {
            if (it.value.hasAgentBound) {
                it.value.view.agent.deactivate()
            }
        }
    }

    override fun onAppEnterForeground() {
        if (requestingPermission) {
            requestingPermission = false
            rebindAgents()
            onAppEnterBackground()
        } else {
            requestAlertWindowPermissionIfNeeded()
        }

        views.forEach {
            if (it.value.hasAgentBound) {
                it.value.view.agent.activate()
            }
        }
    }
}
