package com.kyleduo.aladdin.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.app.databinding.ActivityOkHttpBinding
import com.kyleduo.aladdin.genie.okhttp.api.OkHttpGenie
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.StringListItemDelegate
import kotlinx.coroutines.launch

/**
 * @author kyleduo on 2021/6/30
 */
class OkHttpActivity : AppCompatActivity(), OnItemClickListener<String> {

    private val binding by lazy {
        ActivityOkHttpBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.register(String::class.java, StringListItemDelegate(this))
        }
    }

    private val api = TestApi()

    private val actions = mapOf<String, () -> Unit>(Pair("get", { get() }))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.okhttpActionsList.apply {
            adapter = this@OkHttpActivity.adapter
            layoutManager =
                LinearLayoutManager(this@OkHttpActivity, LinearLayoutManager.VERTICAL, false)
        }

        adapter.items = actions.keys.toList()
    }

    override fun onItemClick(position: Int, item: String) {
        actions[item]?.let {
            it()
        }
    }

    private fun get() = lifecycleScope.launch {
        api.get("value1")
    }
}