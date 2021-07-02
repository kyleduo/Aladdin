package com.kyleduo.aladdin.genie.okhttp.utils

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.kyleduo.aladdin.genie.okhttp.R
import java.net.URL
import java.text.SimpleDateFormat

/**
 * @author kyleduo on 2021/7/2
 */
@Suppress("DEPRECATION")
internal object HttpLogFormatter {
    private val formatter: SimpleDateFormat by lazy { SimpleDateFormat.getDateTimeInstance() as SimpleDateFormat }
    private const val TIME_PATTERN = "HH:mm:ss.SSS"

    /**
     * format startTime in "start: {HH:mm:ss.SSS}" pattern
     */
    fun formatStartTime(context: Context, startTime: Long): CharSequence {
        if (startTime == 0L) {
            return ""
        }
        formatter.applyPattern(TIME_PATTERN)
        val formatted = formatter.format(startTime)

        return SpannableStringBuilder().apply {
            (append(
                context.getString(R.string.aladdin_genie_okhttp_start_time_prefix),
                ForegroundColorSpan(context.resources.getColor(R.color.aladdin_textCaption)),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            ))
            append(formatted)
        }
    }

    /**
     * format duration into "[duration] ms" pattern
     */
    fun formatDuration(context: Context, duration: Long): CharSequence {
        if (duration == 0L) {
            return ""
        }
        return SpannableStringBuilder().apply {
            append(duration.toString())
            append(
                context.getString(R.string.aladdin_genie_okhttp_duration_suffix),
                ForegroundColorSpan(context.resources.getColor(R.color.aladdin_textCaption)),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    /**
     * format URL to SpannableString
     */
    fun formatUrl(context: Context, url: URL): CharSequence {
        return SpannableStringBuilder().apply {
            append(
                url.protocol + "://",
                ForegroundColorSpan(context.resources.getColor(R.color.aladdin_textCaption)),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            append(
                url.authority,
                ForegroundColorSpan(0xFF2391E1.toInt()),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            append(
                url.path,
                ForegroundColorSpan(context.resources.getColor(R.color.aladdin_textBody)),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (url.query.isNotEmpty()) {
                append(
                    "?" + url.query,
                    ForegroundColorSpan(context.resources.getColor(R.color.aladdin_textCaption)),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
}