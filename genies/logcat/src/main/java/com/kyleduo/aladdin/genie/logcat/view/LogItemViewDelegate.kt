package com.kyleduo.aladdin.genie.logcat.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.kyleduo.aladdin.genie.logcat.R
import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.data.LogLevel
import com.kyleduo.aladdin.genie.logcat.databinding.AladdinGenieLogcatItemBinding
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.dp2px
import com.kyleduo.aladdin.ui.inflateView
import kotlin.math.min

/**
 * @author kyleduo on 2021/6/13
 */
class LogItemViewDelegate(
    private val palette: LogItemPalette,
    private val onItemClickListener: OnItemClickListener<LogItem>
) : ItemViewDelegate<LogItem, LogItemViewHolder>() {
    override fun onBindViewHolder(holder: LogItemViewHolder, item: LogItem) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): LogItemViewHolder {
        return LogItemViewHolder(
            parent.inflateView(R.layout.aladdin_genie_logcat_item),
            palette,
            onItemClickListener
        )
    }
}

class LogItemViewHolder(
    itemView: View,
    private val palette: LogItemPalette,
    private val onItemClickListener: OnItemClickListener<LogItem>
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val itemStyles = mutableMapOf<LogLevel, LogItemStyle>()
    }

    private val binding = AladdinGenieLogcatItemBinding.bind(itemView)

    @Suppress("UnnecessaryVariable")
    fun bind(item: LogItem) {
        binding.aladdinLogcatItemLevelBadge.text = item.level.badge
        binding.aladdinLogcatItemTime.text = item.time
        binding.aladdinLogcatItemTag.text = item.tag
        binding.aladdinLogcatItemContent.text = item.content

        val itemStyle = getItemStyle(item.level, palette)

        binding.root.setBackgroundColor(itemStyle.backgroundColor)
        binding.aladdinLogcatItemLevelBadge.background = itemStyle.badgeBackground
        binding.aladdinLogcatItemDivider.setBackgroundColor(itemStyle.dividerColor)

        binding.aladdinLogcatItemLevelBadge.setTextColor(itemStyle.badgeTextColor)

        binding.aladdinLogcatItemTime.setTextColor(itemStyle.textColor)
        binding.aladdinLogcatItemTag.setTextColor(itemStyle.textColor)
        binding.aladdinLogcatItemContent.setTextColor(itemStyle.textColor)

        itemView.setOnClickListener {
            onItemClickListener.onItemClick(absoluteAdapterPosition, item)
        }
    }

    private fun getItemStyle(level: LogLevel, palette: LogItemPalette): LogItemStyle {
        val itemStyle = itemStyles[level]
        if (itemStyle != null) {
            return itemStyle
        }

        val baseColor = palette.getBaseColor(level)
        val hsv = FloatArray(3)
        Color.colorToHSV(baseColor, hsv)

        val backgroundColor =
            Color.HSVToColor(
                floatArrayOf(
                    hsv[0],
                    min(0.2f, hsv[1] * 0.4f),
                    hsv[2] + (1 - hsv[2]) * 0.8f
                )
            )
        val badgeBackgroundColor =
            Color.HSVToColor(
                floatArrayOf(
                    hsv[0],
                    hsv[1] * 0.4f,
                    hsv[2] + (1 - hsv[2]) * 0.5f
                )
            )

        val badgeTextColor =
            Color.HSVToColor(floatArrayOf(hsv[0], hsv[1], hsv[2] * 0.8f))


        val r = 8f.dp2px()

        val badgeBackground = GradientDrawable()
        badgeBackground.shape = GradientDrawable.RECTANGLE
        badgeBackground.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, r, r, 0f, 0f)
        badgeBackground.setColor(badgeBackgroundColor)

        return LogItemStyle(
            baseColor,
            backgroundColor,
            badgeTextColor,
            baseColor,
            badgeBackground
        ).also {
            itemStyles[level] = it
        }
    }

    data class LogItemStyle(
        val textColor: Int,
        val backgroundColor: Int,
        val badgeTextColor: Int,
        val dividerColor: Int,
        val badgeBackground: Drawable
    )
}