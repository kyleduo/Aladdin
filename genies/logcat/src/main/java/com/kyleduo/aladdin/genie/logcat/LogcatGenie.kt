package com.kyleduo.aladdin.genie.logcat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.data.LogLevel
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinLayoutPanelLogcatBinding
import com.kyleduo.aladdin.genie.logcat.filter.LevelFilterPanel
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
    private val itemLimit: Int = 2000,
    private val blacklist: List<String> = listOf("ViewRootImpl"),
    private val logItemPalette: LogItemPalette = DefaultLogItemPalette()
) : AladdinViewGenie(), OnLogItemListener, LevelFilterPanel.OnFilterLevelsSelectedListener {
    companion object {
        const val KEY = "aladdin-genie-logcat"
        const val TAG = "LogcatGenie"
    }

    override val title: String = "Logcat"

    override val key: String = KEY

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.items = filteredItems
            it.register(LogItem::class.java, LogItemViewDelegate(logItemPalette))
        }
    }
    private val layoutManager by lazy {
        LinearLayoutManager(context.app, LinearLayoutManager.VERTICAL, false)
    }

    // all log items
    private var allItems = LinkedList<LogItem>()

    // filtered log items
    private var filteredItems = LinkedList<LogItem>()

    private var filterLevels: List<LogLevel> = listOf(*LogLevel.values())
        set(value) {
            field = value
            binding.aladdinLogcatLevelSelector.text = field.toString()
        }
    private val levelFilterDelegate = lazy {
        LevelFilterPanel(panelController.panelContainer, this)
    }
    private val levelFilter by levelFilterDelegate

    private var reader: LogcatReader? = null

    private lateinit var binding: AladdinLayoutPanelLogcatBinding

    override fun createPanel(container: ViewGroup): View {
        return container.inflateView(R.layout.aladdin_layout_panel_logcat).also { view ->
            binding = AladdinLayoutPanelLogcatBinding.bind(view)

            binding.aladdinLogcatList.also {
                it.adapter = adapter
                it.itemAnimator = null
                it.layoutManager = layoutManager
            }

            binding.aladdinLogcatLevelSelector.setOnClickListener {
                showLevelSelector()
            }

            filterLevels = filterLevels
        }
    }

    override fun onSelected() {
    }

    override fun onDeselected() {
        if (levelFilterDelegate.isInitialized()) {
            levelFilter.dismiss()
        }
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

    private fun showLevelSelector() {
        levelFilter.selectedLevels = filterLevels
        levelFilter.show()
    }

    override fun onLogItem(item: LogItem) {
        allItems.addFirst(item)
        if (allItems.size > itemLimit * 2) {
            allItems = LinkedList(allItems.subList(0, itemLimit))
        }

        if (item.level !in filterLevels) {
            return
        }
        filteredItems.addFirst(item)
        var newList = false
        if (filteredItems.size > itemLimit) {
            filteredItems = LinkedList(filteredItems.subList(0, itemLimit / 2))
            adapter.items = filteredItems
            newList = true
        }

        val isBottom = layoutManager.findFirstVisibleItemPosition() == 0

        if (newList) {
            adapter.notifyDataSetChanged()
        } else {
            adapter.notifyItemInserted(0)
        }

        if (isBottom) {
            binding.aladdinLogcatList.scrollToPosition(0)
        }
    }

    override fun onFilterLevelsSelected(levels: List<LogLevel>) {
        if (levels == filterLevels) {
            return
        }

        filterLevels = levels
        filteredItems.clear()
        filteredItems.addAll(allItems.filter { it.level in filterLevels })
        adapter.notifyDataSetChanged()
        binding.aladdinLogcatList.scrollToPosition(0)
    }
}