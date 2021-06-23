package com.kyleduo.aladdin.genie.hook.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.hook.R
import com.kyleduo.aladdin.genie.hook.data.HookAction
import com.kyleduo.aladdin.genie.hook.data.NoReceiverRefHolder
import com.kyleduo.aladdin.genie.hook.databinding.AladdinGenieHookActionItemBinding
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/17
 */
class HookActionItemViewDelegate(
    private val onItemClickListener: OnItemClickListener<HookAction<Any>>
) : ItemViewDelegate<HookAction<*>, HookActionItemViewHolder>() {
    override fun onBindViewHolder(holder: HookActionItemViewHolder, item: HookAction<*>) {
        @Suppress("UNCHECKED_CAST")
        holder.bind(item as HookAction<Any>)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): HookActionItemViewHolder {
        return HookActionItemViewHolder(
            parent.inflateView(R.layout.aladdin_genie_hook_action_item),
            onItemClickListener
        )
    }
}

class HookActionItemViewHolder(
    itemView: View,
    private val onItemClickListener: OnItemClickListener<HookAction<Any>>
) : RecyclerView.ViewHolder(itemView) {

    private val binding = AladdinGenieHookActionItemBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    fun bind(action: HookAction<in Any>) {
        binding.aladdinGenieHookActionItemName.text = action.title
        binding.aladdinGenieHookActionItemContent.setOnClickListener {
            onItemClickListener.onItemClick(absoluteAdapterPosition, action)
        }
        if (action.reference.get() == NoReceiverRefHolder) {
            binding.aladdinGenieHookActionItemContent.setBackgroundResource(R.drawable.aladdin_genie_hook_no_receiver_action_item_content_bg)
            binding.aladdinGenieHookActionItemReceiver.isVisible = false
        } else {
            binding.aladdinGenieHookActionItemContent.setBackgroundResource(R.drawable.aladdin_genie_hook_action_item_content_bg)
            binding.aladdinGenieHookActionItemReceiver.isVisible = true
            binding.aladdinGenieHookActionItemReceiver.text =
                "[${action.receiverClass.simpleName} #${action.receiverNo}]"
        }
    }
}