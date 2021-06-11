package com.kyleduo.aladdin.genie.appinfo.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.appinfo.R
import com.kyleduo.aladdin.genie.appinfo.databinding.AladdinGenieAppInfoLayoutItemBinding
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/11
 */
class AppInfoItemDelegate : ItemViewDelegate<AppInfoItem, AppInfoItemViewHolder>() {
    override fun onBindViewHolder(
        holder: AppInfoItemViewHolder,
        item: AppInfoItem
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup
    ): AppInfoItemViewHolder {
        return AppInfoItemViewHolder(
            parent.inflateView(R.layout.aladdin_genie_app_info_layout_item)
        )
    }

}

class AppInfoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = AladdinGenieAppInfoLayoutItemBinding.bind(itemView)

    fun bind(item: AppInfoItem) {
        binding.aladdinAppinfoItemNameText.text = item.name
        binding.aladdinAppinfoItemValueText.text = item.value
    }
}