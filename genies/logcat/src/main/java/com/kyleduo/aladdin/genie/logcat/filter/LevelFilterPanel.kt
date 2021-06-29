package com.kyleduo.aladdin.genie.logcat.filter

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.genie.logcat.R
import com.kyleduo.aladdin.genie.logcat.data.LogLevel
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinGenieLogcatItemLevelSelectorBinding
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinGenieLogcatLevelSelectorPanelBinding
import com.kyleduo.aladdin.genie.logcat.view.LogItemStyles
import com.kyleduo.aladdin.ui.*

/**
 * @author kyleduo on 2021/6/22
 */
class LevelFilterPanel(
    container: ViewGroup,
    private val itemStyles: LogItemStyles,
    private val listener: OnFilterLevelsSelectedListener
) : FloatingPanel(container), OnLogLevelSelectedChangeListener {

    private lateinit var binding: AladdinGenieLogcatLevelSelectorPanelBinding
    override val contentLayoutResId: Int = R.layout.aladdin_genie_logcat_level_selector_panel

    override fun onContentInflated(content: View) {
        super.onContentInflated(content)
        binding = AladdinGenieLogcatLevelSelectorPanelBinding.bind(content).also {
            it.aladdinLogcatLevelSelectorList.apply {
                adapter = this@LevelFilterPanel.adapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(SimpleOffsetDecoration(dp2px(8), dp2px(4)))
            }
        }
    }

    var selectedLevels: List<LogLevel> = listOf()

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.register(LogLevelSelectorItem::class, LevelFilterItemViewDelegate(this, itemStyles))
        }
    }

    override fun onShow() {
        super.onShow()
        adapter.items = makeItemList()
        adapter.notifyDataSetChanged()
    }

    private fun makeItemList(): List<LogLevelSelectorItem> {
        return LogLevel.levels.map { LogLevelSelectorItem(it, it in selectedLevels) }
    }

    override fun onLogLevelSelectedChanged(level: LogLevel, isSelected: Boolean) {
        val levels = selectedLevels.toMutableList()
        if (isSelected) {
            levels.add(level)
        } else {
            levels.remove(level)
        }
        levels.sortBy { it.level }
        selectedLevels = levels.toList()
        adapter.items = makeItemList()
        adapter.notifyDataSetChanged()
        listener.onFilterLevelsSelected(selectedLevels)
    }

    override fun onLogLevelFastSelected(level: LogLevel) {
        selectedLevels = LogLevel.levels.filter { it.level >= level.level }
        adapter.items = makeItemList()
        adapter.notifyDataSetChanged()
        listener.onFilterLevelsSelected(selectedLevels)

    }

    interface OnFilterLevelsSelectedListener {

        fun onFilterLevelsSelected(levels: List<LogLevel>)
    }

    class LevelFilterItemViewDelegate(
        private val onLogLevelSelectedChangeListener: OnLogLevelSelectedChangeListener,
        private val itemStyles: LogItemStyles
    ) :
        ItemViewDelegate<LogLevelSelectorItem, LevelFilterItemViewHolder>() {
        override fun onBindViewHolder(
            holder: LevelFilterItemViewHolder,
            item: LogLevelSelectorItem
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup
        ): LevelFilterItemViewHolder {
            return LevelFilterItemViewHolder(
                parent.inflateView(R.layout.aladdin_genie_logcat_item_level_selector),
                onLogLevelSelectedChangeListener,
                itemStyles
            )
        }
    }

    class LevelFilterItemViewHolder(
        itemView: View,
        private val onLogLevelSelectedChangeListener: OnLogLevelSelectedChangeListener,
        private val itemStyles: LogItemStyles
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = AladdinGenieLogcatItemLevelSelectorBinding.bind(itemView)

        fun bind(item: LogLevelSelectorItem) {
            binding.aladdinLogcatLevelSelectorLevel.text = item.level.name

            binding.aladdinLogcatLevelSelectorCb.isSelected = item.isSelected
            binding.aladdinLogcatLevelSelectorCb.setOnClickListener {
                onLogLevelSelectedChangeListener.onLogLevelSelectedChanged(
                    item.level,
                    !item.isSelected
                )
            }

            binding.aladdinLogcatLevelSelectorFastSelect.setOnClickListener {
                onLogLevelSelectedChangeListener.onLogLevelFastSelected(item.level)
            }

            val itemStyle = itemStyles.getStyle(item.level)

            binding.aladdinLogcatLevelSelectorLevel.setTextColor(itemStyle.textColor)
            binding.aladdinLogcatLevelSelectorFastSelect.setColorFilter(itemStyle.textColor)
            binding.aladdinLogcatLevelSelectorCb.imageTintList =
                ColorStateList.valueOf(itemStyle.textColor)

            binding.root.background =
                UIUtils.createRoundCornerDrawable(
                    itemStyle.backgroundColor,
                    4f.dp2px()
                )
        }
    }

    data class LogLevelSelectorItem(
        val level: LogLevel,
        val isSelected: Boolean
    )
}