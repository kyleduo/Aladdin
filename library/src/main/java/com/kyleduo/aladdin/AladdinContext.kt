package com.kyleduo.aladdin

import android.app.Application
import com.kyleduo.aladdin.entry.AladdinEntryManager
import com.kyleduo.aladdin.genies.GenieManager
import com.kyleduo.aladdin.genies.IGenie
import com.kyleduo.aladdin.lifecycle.ActivityLifecycleManager

/**
 * Context during Aladdin runtime.
 *
 * Accessor for several managers, and application resources.
 *
 * @author kyleduo on 2021/5/18
 */
@Suppress("MemberVisibilityCanBePrivate")
class AladdinContext(val application: Application) {
    val entryManager = AladdinEntryManager()
    val activityLifecycleManager = ActivityLifecycleManager()
    val genieManager = GenieManager()

    private var installed = false

    fun addGenie(genie: IGenie): AladdinContext {
        genieManager.addGenie(genie)
        return this
    }

    fun install() {
        if (installed) {
            return
        }
        installed = true
        entryManager.attach()
        activityLifecycleManager.attach()
        genieManager.attach()
    }
}
