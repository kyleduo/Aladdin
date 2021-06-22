package com.kyleduo.aladdin.genie.logcat.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.logcat.R
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinGenieLogcatItemBinding
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/13
 */
class LogItemViewDelegate(
    private val logItemStyles: LogItemStyles,
    private val onItemClickListener: OnItemClickListener<LogItem>
) : ItemViewDelegate<LogItem, LogItemViewHolder>() {
    override fun onBindViewHolder(holder: LogItemViewHolder, item: LogItem) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): LogItemViewHolder {
        return LogItemViewHolder(
            parent.inflateView(R.layout.aladdin_genie_logcat_item),
            logItemStyles,
            onItemClickListener
        )
    }
}

class LogItemViewHolder(
    itemView: View,
    private val logItemStyles: LogItemStyles,
    private val onItemClickListener: OnItemClickListener<LogItem>
) : RecyclerView.ViewHolder(itemView) {

    companion object;

    private val binding = AladdinGenieLogcatItemBinding.bind(itemView)

    @Suppress("UnnecessaryVariable")
    fun bind(item: LogItem) {
        binding.aladdinLogcatItemLevelBadge.text = item.level.badge
        binding.aladdinLogcatItemTime.text = item.time
        binding.aladdinLogcatItemTag.text = item.tag
        binding.aladdinLogcatItemContent.text = item.content

        val itemStyle = logItemStyles.getStyle(item.level)

        binding.root.setBackgroundColor(itemStyle.backgroundColor)
        binding.aladdinLogcatItemLevelBadge.background = itemStyle.badgeBackground
        binding.aladdinLogcatItemDivider.setBackgroundColor(itemStyle.dividerColor)

        binding.aladdinLogcatItemLevelBadge.setTextColor(itemStyle.badgeTextColor)

        binding.aladdinLogcatItemTime.setTextColor(itemStyle.textColor)
        binding.aladdinLogcatItemTag.setTextColor(itemStyle.textColor)
        binding.aladdinLogcatItemContent.setTextColor(itemStyle.textColor)

        itemView.setOnClickListener {
            onItemClickListener.onItemClick(absoluteAdapterPosition, item)
        }
    }
}