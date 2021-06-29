package com.kyleduo.aladdin.api.manager.lifecycle

import android.app.Application
import com.kyleduo.aladdin.api.manager.AladdinManager

/**
 * @author kyleduo on 2021/6/12
 */
interface AladdinLifecycleManager : AladdinManager {

    fun registerAppLifecycleCallbacks(callback: AppLifecycleCallbacks)

    fun registerActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks)
}