package com.kyleduo.aladdin.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

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
    }

}
