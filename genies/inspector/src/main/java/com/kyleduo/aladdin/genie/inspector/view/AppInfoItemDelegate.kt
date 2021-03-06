package com.kyleduo.aladdin.genie.inspector.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.inspector.R
import com.kyleduo.aladdin.genie.inspector.databinding.AladdinGenieInfoLayoutItemBinding
import com.kyleduo.aladdin.ui.inflateView
import com.kyleduo.aladdin.ui.supportCopy

/**
 * @author kyleduo on 2021/6/11
 */
class AppInfoItemDelegate : ItemViewDelegate<InfoItemViewData, AppInfoItemViewHolder>() {
    override fun onBindViewHolder(
        holder: AppInfoItemViewHolder,
        item: InfoItemViewData
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup
    ): AppInfoItemViewHolder {
        return AppInfoItemViewHolder(
            parent.inflateView(R.layout.aladdin_genie_info_layout_item)
        )
    }

}

class AppInfoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = AladdinGenieInfoLayoutItemBinding.bind(itemView).also { binding ->
        binding.aladdinInfoItemValueText.supportCopy {
            it.text
        }
    }

    fun bind(item: InfoItemViewData) {
        binding.aladdinInfoItemNameText.text = item.name
        binding.aladdinInfoItemValueText.text = item.value
    }
}