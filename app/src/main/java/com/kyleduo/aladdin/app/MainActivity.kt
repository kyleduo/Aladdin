package com.kyleduo.aladdin.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.genie.actions.api.ActionsGenie

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.text).setOnClickListener {
            startActivity(Intent(this@MainActivity, NormalActivity::class.java))
        }

        Aladdin.withGenie(ActionsGenie::class.java) {
            it.register(
                "showToastInMain",
                "open activity",
                "Demo",
                this
            ) {
                startActivity(Intent(this, NormalActivity::class.java))
            }

            it.register("goto OkHttpActivity", "open OkHttpActivity", "Demo", this) {
                startActivity(Intent(this, OkHttpActivity::class.java))
            }
        }

        Aladdin.withGenie(ActionsGenie::class.java) {
            it.register("no_receiver", "Log.d()", "No Receiver") {
                Log.d("MainActivity", "from no receiver action")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Aladdin.findGenie(ActionsGenie::class.java)?.discard(this)
    }
}