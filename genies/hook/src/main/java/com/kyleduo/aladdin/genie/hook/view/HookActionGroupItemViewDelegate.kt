package com.kyleduo.aladdin.genie.hook.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.hook.R
import com.kyleduo.aladdin.genie.hook.data.ActionGroupItem
import com.kyleduo.aladdin.genie.hook.databinding.AladdinGenieHookGroupItemBinding
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/24
 */
class HookActionGroupItemViewDelegate(
    private val onItemClickListener: OnItemClickListener<ActionGroupItem>
) : ItemViewDelegate<ActionGroupItem, HookActionGroupItemViewHolder>() {
    override fun onBindViewHolder(holder: HookActionGroupItemViewHolder, item: ActionGroupItem) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup
    ): HookActionGroupItemViewHolder {
        return HookActionGroupItemViewHolder(
            parent.inflateView(R.layout.aladdin_genie_hook_group_item),
            onItemClickListener
        )
    }
}

class HookActionGroupItemViewHolder(
    itemView: View,
    private val onItemClickListener: OnItemClickListener<ActionGroupItem>
) : RecyclerView.ViewHolder(itemView) {
    private val binding = AladdinGenieHookGroupItemBinding.bind(itemView)

    fun bind(group: ActionGroupItem) {
        binding.aladdinGenieHookGroupItemName.text = group.name
        binding.aladdinGenieHookGroupItemArrow.rotation = if (group.isFold) 0f else 90f
        binding.root.setOnClickListener {
            onItemClickListener.onItemClick(absoluteAdapterPosition, group)
        }
    }
}
