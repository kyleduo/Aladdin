package com.kyleduo.aladdin.app

import android.app.Application
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.genie.inspector.api.InfoProvider
import com.kyleduo.aladdin.genie.inspector.api.InspectorGenie
import com.kyleduo.aladdin.genie.inspector.api.data.InfoItem
import com.kyleduo.aladdin.genie.inspector.api.data.InfoSection
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpClientProvider
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpGenie
import okhttp3.OkHttpClient
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

        Aladdin.withGenie<OkHttpGenie> {
            it.registerOkHttpClientProvider(object : OkHttpClientProvider {
                override fun provideClient(): OkHttpClient {
                    val method = ApiClientFactory::class.java.getDeclaredMethod("getClient")
                    method.isAccessible = true
                    val client = method.invoke(ApiClientFactory)
                    return client as OkHttpClient
                }
            })
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
