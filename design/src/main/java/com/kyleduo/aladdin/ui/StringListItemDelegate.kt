package com.kyleduo.aladdin.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate

/**
 * @author kyleduo on 2021/6/30
 */
class StringListItemDelegate(
    private val itemClickListener: OnItemClickListener<String>? = null
) : ItemViewDelegate<String, StringListViewHolder>() {
    override fun onBindViewHolder(holder: StringListViewHolder, item: String) {
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(holder.absoluteAdapterPosition, item)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): StringListViewHolder {
        return StringListViewHolder(parent.inflateView(R.layout.aladdin_design_simple_list_item))
    }
}

class StringListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView = itemView.findViewById(android.R.id.text1)

    fun bind(text: String) {
        textView.text = text
    }
}
