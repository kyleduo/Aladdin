package com.kyleduo.aladdin.genie.logcat

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.data.LogLevel
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinGenieLogcatPanelBinding
import com.kyleduo.aladdin.genie.logcat.detail.LogcatDetailPanel
import com.kyleduo.aladdin.genie.logcat.filter.LevelFilterPanel
import com.kyleduo.aladdin.genie.logcat.reader.LogcatParser
import com.kyleduo.aladdin.genie.logcat.reader.LogcatReader
import com.kyleduo.aladdin.genie.logcat.reader.OnLogItemListener
import com.kyleduo.aladdin.genie.logcat.view.DefaultLogItemPalette
import com.kyleduo.aladdin.genie.logcat.view.LogItemPalette
import com.kyleduo.aladdin.genie.logcat.view.LogItemStyles
import com.kyleduo.aladdin.genie.logcat.view.LogItemViewDelegate
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.UIUtils
import com.kyleduo.aladdin.ui.dp2px
import com.kyleduo.aladdin.ui.inflateView
import java.util.*

/**
 * @author kyleduo on 2021/6/13
 */
class LogcatGenie(
    private val itemLimit: Int = 2000,
    private val blacklist: List<String> = listOf("ViewRootImpl"),
    logItemPalette: LogItemPalette = DefaultLogItemPalette()
) : AladdinViewGenie(), OnLogItemListener, LevelFilterPanel.OnFilterLevelsSelectedListener {
    companion object {
        const val TAG = "LogcatGenie"
    }

    override val title: String = "Logcat"

    private val logItemStyles = LogItemStyles(logItemPalette)

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.items = filteredItems
            it.register(
                LogItem::class.java,
                LogItemViewDelegate(logItemStyles, onItemClickListener)
            )
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
            updateLevelSelector(value)
        }
    private val levelFilterDelegate = lazy {
        LevelFilterPanel(panelController, logItemStyles, this)
    }
    private val levelFilter by levelFilterDelegate

    private var reader: LogcatReader? = null

    private lateinit var binding: AladdinGenieLogcatPanelBinding

    private val logDetailPanel by lazy {
        LogcatDetailPanel(panelController, logItemStyles)
    }

    private val onItemClickListener = object : OnItemClickListener<LogItem> {
        override fun onItemClick(position: Int, item: LogItem) {
            this@LogcatGenie.onItemClick(item)
        }
    }

    override fun createPanel(container: ViewGroup): View {
        return container.inflateView(R.layout.aladdin_genie_logcat_panel).also { view ->
            binding = AladdinGenieLogcatPanelBinding.bind(view)

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

    @SuppressLint("SetTextI18n")
    private fun updateLevelSelector(levels: List<LogLevel>) {
        if (levels.isEmpty()) {
            binding.aladdinLogcatLevelSelector.text = "None"
            binding.aladdinLogcatLevelSelector.background = UIUtils.createRoundCornerDrawable(
                logItemStyles.getStyle(LogLevel.Verbose).backgroundColor,
                4f.dp2px()
            )
            return
        }
        val ssb = SpannableStringBuilder()
        val first = levels.first()
        when (levels.size) {
            LogLevel.Assert.level - first.level + 1 -> ssb.append(
                first.name + "+",
                ForegroundColorSpan(logItemStyles.getStyle(first).textColor),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            1 -> ssb.append(
                first.name,
                ForegroundColorSpan(logItemStyles.getStyle(first).textColor),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            else -> {
                for (level in levels) {
                    ssb.append(
                        level.name.substring(0, 1) + "/",
                        ForegroundColorSpan(logItemStyles.getStyle(level).textColor),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                ssb.delete(ssb.length - 1, ssb.length)
            }
        }
        binding.aladdinLogcatLevelSelector.text = ssb
        binding.aladdinLogcatLevelSelector.background = UIUtils.createRoundCornerDrawable(
            logItemStyles.getStyle(first).backgroundColor,
            4f.dp2px()
        )
    }

    private fun showLevelSelector() {
        levelFilter.selectedLevels = filterLevels
        levelFilter.show()
    }

    override fun onLogItem(item: LogItem) {
        if (allItems.isNotEmpty()) {
            val first = allItems.first
            if (first.maybeMerge(item)) {
                val merged = first.merge(item)
                allItems[0] = merged
                if (filteredItems[0] == first) {
                    filteredItems[0] = merged
                }
                adapter.notifyItemChanged(0)
                return
            }
        }
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

        val isAtTop = layoutManager.findFirstVisibleItemPosition() == 0

        if (newList) {
            adapter.notifyDataSetChanged()
        } else {
            adapter.notifyItemInserted(0)
        }

        if (isAtTop) {
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

    private fun onItemClick(item: LogItem) {
        logDetailPanel.item = item
        logDetailPanel.show()
    }
}