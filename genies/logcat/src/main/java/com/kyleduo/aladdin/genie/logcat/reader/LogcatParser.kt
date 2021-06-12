package com.kyleduo.aladdin.genie.logcat.reader

import com.kyleduo.aladdin.genie.logcat.data.LogItem
import com.kyleduo.aladdin.genie.logcat.data.LogLevel

/**
 * @author kyleduo on 2021/6/13
 */
class LogcatParser {

    companion object {
        // 06-13 01:24:51.691  2537  2731 D NetworkController.SecMobileSignalController(0/2147483647): onSignalStrengthsChanged level=2
        private const val REGEX =
            """^(\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3})\s+(\d+)\s+(\d+)\s+(\S)\s+([^:\s]+)\s*:\s+(.+)$"""

        val regex = Regex(REGEX)
    }

    fun parse(message: String): LogItem {
        val result = regex.find(message) ?: return LogItem.error("Parse Error", message)

        if (result.groups.size != 7) {
            // not valid
            return LogItem.error("Parse Error", message)
        }

        val time = result.groups[1]?.value ?: ""
//        val pid = result.groups[2]?.value ?: ""
        val tid = result.groups[3]?.value ?: ""
        val level = LogLevel.parse(result.groups[4]?.value ?: "")
        val tag = result.groups[5]?.value ?: ""
        val content = result.groups[6]?.value ?: ""

        return LogItem(level, time, tid, tag, content)
    }
}