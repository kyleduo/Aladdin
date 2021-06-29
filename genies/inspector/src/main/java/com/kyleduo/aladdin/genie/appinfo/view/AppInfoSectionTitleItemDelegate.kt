package com.kyleduo.aladdin.genie.appinfo.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.appinfo.R
import com.kyleduo.aladdin.genie.appinfo.databinding.AladdinGenieInfoLayoutSectionTitleBinding
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/6/11
 */
class AppInfoSectionTitleItemDelegate :
    ItemViewDelegate<InfoSectionTitleViewData, AppInfoSectionTitleViewHolder>() {
    override fun onBindViewHolder(
        holder: AppInfoSectionTitleViewHolder,
        item: InfoSectionTitleViewData
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup
    ): AppInfoSectionTitleViewHolder {
        return AppInfoSectionTitleViewHolder(
            parent.inflateView(R.layout.aladdin_genie_info_layout_section_title)
        )
    }

}

class AppInfoSectionTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = AladdinGenieInfoLayoutSectionTitleBinding.bind(itemView)

    fun bind(item: InfoSectionTitleViewData) {
        binding.aladdinInfoSectionTitleText.text = item.title
        binding.aladdinInfoSectionDescText.text = item.desc
    }
}