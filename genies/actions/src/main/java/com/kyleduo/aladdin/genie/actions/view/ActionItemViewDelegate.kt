package com.kyleduo.aladdin.genie.actions.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.actions.R
import com.kyleduo.aladdin.genie.actions.data.Action
import com.kyleduo.aladdin.genie.actions.data.NoReceiverRefHolder
import com.kyleduo.aladdin.genie.actions.databinding.AladdinGenieActionsActionItemBinding
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/17
 */
class HookActionItemViewDelegate(
    private val onItemClickListener: OnItemClickListener<Action<Any>>
) : ItemViewDelegate<Action<*>, HookActionItemViewHolder>() {
    override fun onBindViewHolder(holder: HookActionItemViewHolder, item: Action<*>) {
        @Suppress("UNCHECKED_CAST")
        holder.bind(item as Action<Any>)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): HookActionItemViewHolder {
        return HookActionItemViewHolder(
            parent.inflateView(R.layout.aladdin_genie_actions_action_item),
            onItemClickListener
        )
    }
}

class HookActionItemViewHolder(
    itemView: View,
    private val onItemClickListener: OnItemClickListener<Action<Any>>
) : RecyclerView.ViewHolder(itemView) {

    private val binding = AladdinGenieActionsActionItemBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    fun bind(action: Action<in Any>) {
        binding.aladdinGenieActionsActionItemName.text = action.title
        binding.aladdinGenieActionsActionItemContent.setOnClickListener {
            onItemClickListener.onItemClick(absoluteAdapterPosition, action)
        }
        if (action.reference.get() == NoReceiverRefHolder) {
            binding.aladdinGenieActionsActionItemContent.setBackgroundResource(R.drawable.aladdin_genie_actions_no_receiver_action_item_content_bg)
            binding.aladdinGenieActionsActionItemReceiver.isVisible = false
        } else {
            binding.aladdinGenieActionsActionItemContent.setBackgroundResource(R.drawable.aladdin_genie_actions_action_item_content_bg)
            binding.aladdinGenieActionsActionItemReceiver.isVisible = true
            binding.aladdinGenieActionsActionItemReceiver.text =
                "[${action.receiverClass.simpleName} #${action.receiverNo}]"
        }
    }
}