package com.kyleduo.aladdin.app

import android.app.Application
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.genie.appinfo.api.InfoProvider
import com.kyleduo.aladdin.genie.appinfo.api.InspectorGenie
import com.kyleduo.aladdin.genie.appinfo.api.data.InfoItem
import com.kyleduo.aladdin.genie.appinfo.api.data.InfoSection
import kotlin.random.Random

/**
 * @author kyleduo on 2021/5/18
 */
class DemoApplication : Application(), InfoProvider {

    override fun onCreate() {
        super.onCreate()

        AladdinInitializer.initialize(this)

        Aladdin.withGenie<InspectorGenie> {
            it.registerInfoProvider(this)
        }
    }

    private val random = Random(System.currentTimeMillis())

    override fun provideInfo(): InfoSection {
        return InfoSection(
            "Demo Application", "registered in App",
            listOf(
                InfoItem("Random", "${random.nextInt(10000, 20000)}")
            )
        )
    }
}
