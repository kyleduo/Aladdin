package com.kyleduo.aladdin.genie.logcat.data

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author kyleduo on 2021/6/13
 */
data class LogItem(
    val level: LogLevel,
    val time: String,
    val tid: String,
    val tag: String,
    val content: String,
    val raw: String
) {
    companion object {
        private val formatter: SimpleDateFormat by lazy {
            (SimpleDateFormat.getDateTimeInstance() as SimpleDateFormat).also {
                it.applyPattern("MM-dd HH:mm:ss.SSS")
            }
        }

        fun error(reason: String, origin: String): LogItem {
            return LogItem(LogLevel.Error, formatter.format(Date()), "", reason, origin, origin)
        }
    }


    fun canMerge(another: LogItem): Boolean {
        return level == another.level &&
                time == another.time &&
                tid == another.tid &&
                tag == another.tag
    }

    fun merge(another: LogItem): LogItem {
        return copy(content = "$content\n${another.content}", raw = "$raw\n${another.raw}")
    }
}
