package com.kyleduo.aladdin.api.config

import android.app.Activity

/**
 * @author kyleduo on 2021/6/12
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class AladdinLifecycleConfigurator {

    val activityWhitelist: MutableList<Class<Activity>> = mutableListOf()
    val activityBlacklist: MutableList<Class<Activity>> = mutableListOf()

    fun activityWhitelist(list: List<Class<Activity>>): AladdinLifecycleConfigurator {
        activityWhitelist.addAll(list)
        return this
    }

    fun activityBlacklist(list: List<Class<Activity>>): AladdinLifecycleConfigurator {
        activityBlacklist.addAll(list)
        return this
    }
}