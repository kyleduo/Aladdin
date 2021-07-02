package com.kyleduo.aladdin.genie.okhttp.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.okhttp.R
import com.kyleduo.aladdin.genie.okhttp.data.HttpLog
import com.kyleduo.aladdin.genie.okhttp.databinding.AladdinGenieOkhttpLogItemBinding
import com.kyleduo.aladdin.genie.okhttp.utils.HttpLogFormatter
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.UIUtils
import com.kyleduo.aladdin.ui.dp2px
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

    companion object {

        private const val RESPONSE_BODY_LIMIT = 200
    }

    private val binding = AladdinGenieOkhttpLogItemBinding.bind(itemView)

    fun bind(log: HttpLog) {
        val finished = log.response != null
        val response = log.response
        val statusColor = when {
            response == null -> 0xFF3B84DB.toInt()
            response.statusCode / 100 == 2 -> 0xFF21B818.toInt()
            else -> 0xFFE75656.toInt()
        }
        binding.aladdinGenieOkhttpLogItemStatus.background =
            UIUtils.createRoundCornerDrawable(statusColor, itemView.dp2px(4f))
        binding.aladdinGenieOkhttpLogItemStatus.text = response?.statusCode?.toString() ?: "..."
        binding.aladdinGenieOkhttpLogDetailStart.text =
            HttpLogFormatter.formatStartTime(binding.root.context, log.startTime)
        binding.aladdinGenieOkhttpLogDetailDuration.text =
            HttpLogFormatter.formatDuration(binding.root.context, log.durationInMs)
        binding.aladdinGenieOkhttpLogItemMethod.text = log.request.method
        binding.aladdinGenieOkhttpLogItemUrl.text =
            HttpLogFormatter.formatUrl(binding.root.context, log.request.url)

        binding.aladdinGenieOkhttpLogItemResponseIcon.isVisible = finished
        binding.aladdinGenieOkhttpLogItemResponse.isVisible = finished

        if (finished) {
            val body = response?.body?.let {
                if (it.length > RESPONSE_BODY_LIMIT) {
                    it.subSequence(0, RESPONSE_BODY_LIMIT).toString()
                } else {
                    it
                }.replace("\n", " ")
            }
            binding.aladdinGenieOkhttpLogItemResponse.text = (body ?: response?.errorMessage ?: "")
        }
    }
}