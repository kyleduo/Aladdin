package com.kyleduo.aladdin.app

import android.app.Application
import com.kyleduo.aladdin.Aladdin

/**
 * @author kyleduo on 2021/5/18
 */
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Aladdin.with(this).install()
    }
}
