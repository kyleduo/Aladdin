package com.kyleduo.aladdin.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.genie.actions.api.ActionsGenie
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author kyleduo on 2021/6/9
 */
class NormalActivity : AppCompatActivity() {

    companion object {
        val counter = AtomicInteger()
    }

    val index = counter.getAndIncrement()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        findViewById<View>(R.id.text).setOnClickListener {
            startActivity(Intent(this@NormalActivity, NormalActivity::class.java))
        }

        Aladdin.findGenie<ActionsGenie>()?.let {
            it.register("showToast", "showToast", "Demo", this) { r ->
                r.showToast()
            }
        }
    }

    private fun showToast() {
        Toast.makeText(this, "Normal Activity - $index", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Aladdin.findGenie<ActionsGenie>()?.unregister(
            "showToast",
            this
        )
    }
}
