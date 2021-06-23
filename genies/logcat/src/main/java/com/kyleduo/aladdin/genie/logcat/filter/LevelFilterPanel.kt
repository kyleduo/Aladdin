package com.kyleduo.aladdin.genie.logcat.filter

import android.content.Context
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
import com.kyleduo.aladdin.ui.inflateView
import com.kyleduo.aladdin.ui.layoutInflater

/**
 * @author kyleduo on 2021/6/22
 */
class LevelFilterPanel(
    private val container: ViewGroup,
    private val listener: OnFilterLevelsSelectedListener
) : OnLogLevelSelectedChangeListener {

    private val binding by lazy {
        AladdinGenieLogcatLevelSelectorPanelBinding.inflate(
            container.context.layoutInflater(),
            container,
            false
        ).also {
            it.aladdinLogcatLevelSelectorClose.setOnClickListener {
                dismiss()
            }
            it.aladdinLogcatLevelSelectorList.apply {
                adapter = this@LevelFilterPanel.adapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    var selectedLevels: List<LogLevel> = listOf()

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.register(LogLevelSelectorItem::class, LevelFilterItemViewDelegate(this))
        }
    }

    fun show() {
        val root = binding.root
        (root.parent as? ViewGroup)?.removeView(root)

        container.addView(root)
        adapter.items = makeItemList()
        adapter.notifyDataSetChanged()
    }

    fun dismiss() {
        val root = binding.root
        (root.parent as? ViewGroup)?.removeView(root)
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
        private val onLogLevelSelectedChangeListener: OnLogLevelSelectedChangeListener
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
                onLogLevelSelectedChangeListener
            )
        }
    }

    class LevelFilterItemViewHolder(
        itemView: View,
        private val onLogLevelSelectedChangeListener: OnLogLevelSelectedChangeListener
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = AladdinGenieLogcatItemLevelSelectorBinding.bind(itemView)

        fun bind(item: LogLevelSelectorItem) {
            binding.aladdinLogcatLevelSelectorLevel.text = item.level.name

            fun muteCheckBox(action: () -> Unit) {
                binding.aladdinLogcatLevelSelectorCheckbox.setOnCheckedChangeListener(null)
                action()
                binding.aladdinLogcatLevelSelectorCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    onLogLevelSelectedChangeListener.onLogLevelSelectedChanged(
                        item.level,
                        isChecked
                    )
                }
            }

            muteCheckBox {
                binding.aladdinLogcatLevelSelectorCheckbox.isChecked = item.isSelected
            }

            binding.aladdinLogcatLevelSelectorFastSelect.setOnClickListener {
                onLogLevelSelectedChangeListener.onLogLevelFastSelected(item.level)
            }
        }
    }

    data class LogLevelSelectorItem(
        val level: LogLevel,
        val isSelected: Boolean
    )
}