package com.kyleduo.aladdin

import android.app.Application
import com.kyleduo.aladdin.api.AladdinConfigurator
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.config.AladdinGenieConfigurator
import com.kyleduo.aladdin.api.config.AladdinLifecycleConfigurator
import com.kyleduo.aladdin.api.config.AladdinViewConfigurator
import com.kyleduo.aladdin.managers.AladdinGenieManagerImpl
import com.kyleduo.aladdin.managers.AladdinLifecycleManagerImpl
import com.kyleduo.aladdin.managers.view.AladdinViewManagerImpl

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

    override val lifecycleManager = AladdinLifecycleManagerImpl(
        this,
        aladdinConfigurator.getConfigurator(AladdinLifecycleConfigurator::class.java)
    )
    override val viewManager =
        AladdinViewManagerImpl(this, aladdinConfigurator.getConfigurator(AladdinViewConfigurator::class.java))
    override val genieManager =
        AladdinGenieManagerImpl(this, aladdinConfigurator.getConfigurator(AladdinGenieConfigurator::class.java))
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
