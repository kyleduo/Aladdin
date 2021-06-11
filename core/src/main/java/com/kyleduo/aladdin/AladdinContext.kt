package com.kyleduo.aladdin

import android.app.Application
import com.kyleduo.aladdin.board.BoardGenie
import com.kyleduo.aladdin.entry.EntryGenie
import com.kyleduo.aladdin.genies.GenieManager
import com.kyleduo.aladdin.genies.IGenie
import com.kyleduo.aladdin.lifecycle.ActivityLifecycleManager
import com.kyleduo.aladdin.view.AladdinViewManager

/**
 * Context during Aladdin runtime.
 *
 * Accessor for several managers, and application resources.
 *
 * @author kyleduo on 2021/5/18
 */
@Suppress("MemberVisibilityCanBePrivate")
class AladdinContext(val application: Application) {
    val activityLifecycleManager = ActivityLifecycleManager()
    val genieManager = GenieManager()
    val viewManager = AladdinViewManager()

    private var installed = false

    init {
        addGenie(EntryGenie())
        addGenie(BoardGenie())
    }

    fun addGenie(genie: IGenie): AladdinContext {
        genieManager.addGenie(genie)
        return this
    }

    fun install() {
        if (installed) {
            return
        }
        installed = true

        val managers = listOf(
            activityLifecycleManager,
            genieManager,
            viewManager,
        )

        managers.forEach { it.attach() }
        managers.forEach { it.ready() }
    }
}
