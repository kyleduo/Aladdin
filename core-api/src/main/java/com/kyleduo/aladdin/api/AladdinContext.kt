package com.kyleduo.aladdin.api

import android.app.Application
import com.kyleduo.aladdin.api.manager.genie.GenieManager
import com.kyleduo.aladdin.api.manager.lifecycle.LifecycleManager
import com.kyleduo.aladdin.api.manager.view.ViewManager

/**
 * Context for Aladdin implementation and extensions.
 *
 * @author kyleduo on 2021/6/12
 */
interface AladdinContext {

    val app: Application

    val lifecycleManager: LifecycleManager

    val viewManager: ViewManager

    val genieManager: GenieManager

    fun install()
}