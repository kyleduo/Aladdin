package com.kyleduo.aladdin.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

        (Aladdin.context.genieManager.findGenie("aladdin-genie-hook") as? HookGenie)?.let {
            it.register("showToastInMain", "[main]showToast", "Demo - Main", this) { r ->
                r.showToast()
            }
        }

        (Aladdin.context.genieManager.findGenie("aladdin-genie-hook") as? HookGenie)?.let {
            it.register("no_receiver", "no receiver", "Demo - Main") {
                Log.d("MainActivity", "from no receiver action")
            }
        }
    }

    private fun showToast() {
        Toast.makeText(this, "[main] toast from hook action", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        (Aladdin.context.genieManager.findGenie("aladdin-genie-hook") as? HookGenie)?.unregister(
            "showToastInMain",
            this
        )
    }
}