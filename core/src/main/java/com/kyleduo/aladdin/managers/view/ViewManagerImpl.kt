package com.kyleduo.aladdin.managers.view

import android.widget.Toast
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.config.ViewConfigurator
import com.kyleduo.aladdin.api.manager.lifecycle.AppLifecycleCallbacks
import com.kyleduo.aladdin.api.manager.view.AladdinView
import com.kyleduo.aladdin.api.manager.view.AladdinViewAgent
import com.kyleduo.aladdin.api.manager.view.ViewManager
import com.kyleduo.aladdin.api.manager.view.ViewMode
import com.kyleduo.aladdin.managers.view.agent.AdaptViewAgent
import com.kyleduo.aladdin.managers.view.agent.GlobalViewAgent
import com.kyleduo.aladdin.utils.PermissionUtils

/**
 * A manager for organizing [AladdinView]'s.
 *
 * @author kyleduo on 2021/5/25
 */
class ViewManagerImpl(
    val context: AladdinContext,
    configurator: ViewConfigurator?
) : ViewManager, AppLifecycleCallbacks {

    override var mode: ViewMode = configurator?.viewMode ?: ViewMode.Global

    private val views = mutableMapOf<Any, AladdinViewEntry>()
    private val viewPositionStorage = ViewPositionStorage(context)
    private var requestingPermission = false

    override fun register(view: AladdinView) {
        val entry = AladdinViewEntry(view, createViewAgent(), false)
        views[view.tag] = entry
    }

    override fun attach() {
        context.lifecycleManager.registerAppLifecycleCallbacks(this)
    }

    override fun ready() {
        rebindAgents()
    }

    private fun createViewAgent(): AladdinViewAgent {
        return if (mode == ViewMode.Global) {
            GlobalViewAgent(context, viewPositionStorage)
        } else {
            AdaptViewAgent(context, viewPositionStorage)
        }
    }

    private fun requestAlertWindowPermissionIfNeeded() {
        if (mode == ViewMode.Global) {
            if (!PermissionUtils.hasAlertWindowPermission(context.app)) {
                Toast.makeText(
                    context.app,
                    "Need System Alert Window permission in Global mode.",
                    Toast.LENGTH_SHORT
                ).show()
                requestingPermission = true
                PermissionUtils.requestAlertWindowPermission(context.app)
            }
        }
    }

    private fun rebindAgents() {
        if (mode == ViewMode.Adaptive || PermissionUtils.hasAlertWindowPermission(context.app)) {
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
