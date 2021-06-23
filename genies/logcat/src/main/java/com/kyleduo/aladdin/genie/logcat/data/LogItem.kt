package com.kyleduo.aladdin.genie.logcat.data

/**
 * @author kyleduo on 2021/6/13
 */
data class LogItem(
    val level: LogLevel,
    val time: String,
    val tid: String,
    val tag: String,
    val content: String
) {
    companion object {
        fun error(reason: String, origin: String): LogItem {
            return LogItem(LogLevel.Error, "", "", reason, origin)
        }
    }

    fun maybeMerge(another: LogItem): Boolean {
        return level == another.level &&
                time == another.time &&
                tid == another.tid &&
                tag == another.tag
    }

    fun merge(another: LogItem): LogItem {
        return copy(content = "$content\n${another.content}")
    }
}
