package com.kyleduo.aladdin.genie.hook.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.hook.R
import com.kyleduo.aladdin.genie.hook.data.HookAction
import com.kyleduo.aladdin.genie.hook.databinding.AladdinGenieHookActionItemBinding
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/17
 */
class HookActionItemViewDelegate : ItemViewDelegate<HookAction<*>, HookActionItemViewHolder>() {
    override fun onBindViewHolder(holder: HookActionItemViewHolder, item: HookAction<*>) {
        @Suppress("UNCHECKED_CAST")
        holder.bind(item as HookAction<Any>)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): HookActionItemViewHolder {
        return HookActionItemViewHolder(parent.inflateView(R.layout.aladdin_genie_hook_action_item))
    }
}

class HookActionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = AladdinGenieHookActionItemBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    fun bind(item: HookAction<in Any>) {
        val instance = item.reference.get()
        val hash = instance?.hashCode()?.toString() ?: "Recycled"
        binding.aladdinGenieHookActionItemName.text = item.title + " " + hash
        binding.aladdinGenieHookActionItemName.setOnClickListener {
            item.reference.get()?.let { r -> item.action(r) }
        }
    }
}