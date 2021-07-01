package com.kyleduo.aladdin.genie.okhttp.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.okhttp.R
import com.kyleduo.aladdin.genie.okhttp.data.HttpLog
import com.kyleduo.aladdin.genie.okhttp.databinding.AladdinGenieOkhttpLogItemBinding
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.inflateView

/**
 * @author kyleduo on 2021/7/1
 */
class HttpLogItemViewDelegate(
    private val onItemClickListener: OnItemClickListener<HttpLog>
) : ItemViewDelegate<HttpLog, HttpLogItemViewHolder>() {
    override fun onBindViewHolder(holder: HttpLogItemViewHolder, item: HttpLog) {
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(holder.absoluteAdapterPosition, item)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): HttpLogItemViewHolder {
        return HttpLogItemViewHolder(parent.inflateView(R.layout.aladdin_genie_okhttp_log_item))
    }
}

class HttpLogItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = AladdinGenieOkhttpLogItemBinding.bind(itemView)

    fun bind(log: HttpLog) {
        binding.aladdinGenieOkhttpLogItemTest.text = log.toString()
    }
}