package com.kyleduo.aladdin.genie.hook.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.hook.R
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/24
 */
class HookActionGroupItemViewDelegate : ItemViewDelegate<String, HookActionGroupItemViewHolder>() {
    override fun onBindViewHolder(holder: HookActionGroupItemViewHolder, item: String) {
        holder.nameView.text = item
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup
    ): HookActionGroupItemViewHolder {
        return HookActionGroupItemViewHolder(parent.inflateView(R.layout.aladdin_genie_hook_group_item))
    }

}

class HookActionGroupItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameView: TextView = itemView.findViewById(R.id.aladdin_genie_hook_group_item_name)
}
