package com.kyleduo.aladdin.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.genie.hook.HookGenie

/**
 * @author kyleduo on 2021/6/9
 */
class NormalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        findViewById<View>(R.id.text).setOnClickListener {
            startActivity(Intent(this@NormalActivity, NormalActivity::class.java))
        }

        (Aladdin.context.genieManager.findGenie("aladdin-genie-hook") as? HookGenie)?.let {
            it.register("showToast", "showToast", this) { r ->
                r.showToast()
            }
        }

    }

    private fun showToast() {
        Toast.makeText(this, "toast from hook action", Toast.LENGTH_SHORT).show()
    }
}
