package com.kyleduo.aladdin.genie.logcat.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.logcat.R
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinItemLogcatBinding
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/13
 */
class LogItemViewDelegate : ItemViewDelegate<LogItem, LogItemViewHolder>() {
    override fun onBindViewHolder(holder: LogItemViewHolder, item: LogItem) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): LogItemViewHolder {
        return LogItemViewHolder(parent.inflateView(R.layout.aladdin_item_logcat))
    }
}

class LogItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = AladdinItemLogcatBinding.bind(itemView)

    fun bind(item: LogItem) {
        binding.aladdinLogcatItemLevelBadge.text = item.level.badge
        binding.aladdinLogcatItemTime.text = item.time
        binding.aladdinLogcatItemTag.text = item.tag
        binding.aladdinLogcatItemContent.text = item.content
    }
}