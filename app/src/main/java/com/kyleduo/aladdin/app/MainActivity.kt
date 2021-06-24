package com.kyleduo.aladdin.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.genie.hook.HookGenie

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.text).setOnClickListener {
            startActivity(Intent(this@MainActivity, NormalActivity::class.java))
        }

        Aladdin.findGenie<HookGenie>(HookGenie.KEY)?.let {
            for (i in 1..10) {
                it.register(
                    "showToastInMain - $i",
                    "open activity [$i]",
                    "Demo - Main - ${i / 5}",
                    this
                ) { r ->
                    r.startActivity(Intent(r, NormalActivity::class.java))
                }
            }

            it.register(
                "showToastInMain",
                "open activity",
                "",
                this
            ) { r ->
                r.startActivity(Intent(r, NormalActivity::class.java))
            }
        }

        Aladdin.findGenie<HookGenie>(HookGenie.KEY)?.let {
            it.register("no_receiver", "no receiver", "Demo - Main") {
                Log.d("MainActivity", "from no receiver action")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Aladdin.findGenie<HookGenie>(HookGenie.KEY)?.unregister(
            "showToastInMain",
            this
        )
    }
}