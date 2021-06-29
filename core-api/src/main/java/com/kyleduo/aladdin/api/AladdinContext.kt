package com.kyleduo.aladdin.api

import android.app.Application
import com.kyleduo.aladdin.api.manager.genie.AladdinGenieManager
import com.kyleduo.aladdin.api.manager.lifecycle.AladdinLifecycleManager
import com.kyleduo.aladdin.api.manager.view.AladdinViewManager

/**
 * Context for Aladdin implementation and extensions.
 *
 * @author kyleduo on 2021/6/12
 */
interface AladdinContext {

    val app: Application

    val lifecycleManager: AladdinLifecycleManager

    val viewManager: AladdinViewManager

    val genieManager: AladdinGenieManager

    fun install()
}