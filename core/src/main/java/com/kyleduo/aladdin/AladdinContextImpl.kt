package com.kyleduo.aladdin

import android.app.Application
import com.kyleduo.aladdin.api.AladdinConfigurator
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.config.GenieConfigurator
import com.kyleduo.aladdin.api.config.LifecycleConfigurator
import com.kyleduo.aladdin.api.config.ViewConfigurator
import com.kyleduo.aladdin.managers.GenieManagerImpl
import com.kyleduo.aladdin.managers.LifecycleManagerImpl
import com.kyleduo.aladdin.managers.view.ViewManagerImpl

/**
 * Context during Aladdin runtime.
 *
 * Accessor for several managers, and application resources.
 *
 * @author kyleduo on 2021/5/18
 */
@Suppress("MemberVisibilityCanBePrivate")
class AladdinContextImpl(aladdinConfigurator: AladdinConfigurator) : AladdinContext {

    override val app: Application = aladdinConfigurator.application

    override val lifecycleManager = LifecycleManagerImpl(
        this,
        aladdinConfigurator.getConfigurator(LifecycleConfigurator::class.java)
    )
    override val viewManager =
        ViewManagerImpl(this, aladdinConfigurator.getConfigurator(ViewConfigurator::class.java))
    override val genieManager =
        GenieManagerImpl(this, aladdinConfigurator.getConfigurator(GenieConfigurator::class.java))
    private var installed = false

    override fun install() {
        if (installed) {
            return
        }
        installed = true

        val managers = listOf(
            lifecycleManager,
            genieManager,
            viewManager,
        )

        managers.forEach { it.attach() }
        managers.forEach { it.ready() }
    }
}
