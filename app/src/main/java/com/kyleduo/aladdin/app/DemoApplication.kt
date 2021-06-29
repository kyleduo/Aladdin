package com.kyleduo.aladdin.app

import android.app.Application

/**
 * @author kyleduo on 2021/5/18
 */
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AladdinInitializer.initialize(this)
    }
}
