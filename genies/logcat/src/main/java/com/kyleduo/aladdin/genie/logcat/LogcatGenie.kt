package com.kyleduo.aladdin.genie.logcat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinLayoutPanelLogcatBinding
import com.kyleduo.aladdin.genie.logcat.reader.LogcatParser
import com.kyleduo.aladdin.genie.logcat.reader.LogcatReader
import com.kyleduo.aladdin.genie.logcat.reader.OnLogItemListener
import com.kyleduo.aladdin.genie.logcat.view.DefaultLogItemPalette
import com.kyleduo.aladdin.genie.logcat.view.LogItemPalette
import com.kyleduo.aladdin.genie.logcat.view.LogItemViewDelegate
import com.kyleduo.aladdin.ui.inflateView
import java.util.*

/**
 * @author kyleduo on 2021/6/13
 */
class LogcatGenie(
    context: AladdinContext,
    private val itemLimit: Int = 2000,
    private val blacklist: List<String> = listOf("ViewRootImpl"),
    private val logItemPalette: LogItemPalette = DefaultLogItemPalette()
) : AladdinViewGenie(context),
    OnLogItemListener {
    companion object {
        const val KEY = "aladdin-genie-logcat"
        const val TAG = "LogcatGenie"
    }

    override val title: String = "Logcat"

    override val key: String = KEY

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.items = items
            it.register(LogItem::class.java, LogItemViewDelegate(logItemPalette))
        }
    }
    private val layoutManager by lazy {
        LinearLayoutManager(context.app, LinearLayoutManager.VERTICAL, false)
    }

    private var items = LinkedList<LogItem>()

    private var reader: LogcatReader? = null

    private lateinit var binding: AladdinLayoutPanelLogcatBinding

    override fun createPanel(container: ViewGroup): View {
        return container.inflateView(R.layout.aladdin_layout_panel_logcat).also { view ->
            binding = AladdinLayoutPanelLogcatBinding.bind(view)

            binding.aladdinLogcatList.also {
                it.adapter = adapter
                it.layoutManager = layoutManager
            }
        }
    }

    override fun onSelected() {
    }

    override fun onDeselected() {
    }

    override fun onStart() {
        start()
    }

    override fun onStop() {
        stop()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun start() {
        stop()
        reader = LogcatReader(LogcatParser(), blacklist, this).also {
            it.start()
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun stop() {
        reader?.interrupt()
        reader = null
    }

    override fun onLogItem(item: LogItem) {
        val isBottom = layoutManager.findLastVisibleItemPosition() == items.size - 1
        items.addFirst(item)
        if (items.size > itemLimit * 2) {
            items = LinkedList(items.subList(0, itemLimit))
            adapter.items = items
        }
        adapter.notifyDataSetChanged()
        if (isBottom) {
            binding.aladdinLogcatList.scrollToPosition(0)
        }
    }
}