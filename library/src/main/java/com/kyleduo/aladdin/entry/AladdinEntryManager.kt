package com.kyleduo.aladdin.entry

import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.lifecycle.OnAppBackgroundListener
import com.kyleduo.aladdin.lifecycle.OnAppForegroundListener
import com.kyleduo.aladdin.structure.IAladdinManager
import com.kyleduo.aladdin.utils.PermissionUtils

/**
 * Manager for AladdinEntry.
 *
 * Listening lifecycle callbacks of Activity and show/hide entry.
 *
 * @author kyleduo on 2021/5/18
 */
class AladdinEntryManager : IAladdinManager, OnAppBackgroundListener, OnAppForegroundListener {

    private val entry = AladdinEntry()

    override fun attach() {
        Aladdin.context.activityLifecycleManager.addOnAppBackgroundListener(this)
        Aladdin.context.activityLifecycleManager.addOnAppForegroundListener(this)

        // TODO: 2021/5/18 kyleduo add view to window or request permission if permission not meet
        if (!PermissionUtils.hasAlertWindowPermission()) {
            PermissionUtils.requestAlertWindowPermission()
        } else {
            entry.show()
        }
    }

    override fun onEnterBackground() {

    }

    override fun onEnterForeground() {

    }
}
