package com.kyleduo.aladdin.genie.logcat.view

import com.kyleduo.aladdin.genie.logcat.data.LogLevel

/**
 * @author kyleduo on 2021/6/13
 */
open class DefaultLogItemPalette : LogItemPalette {

    private val level2Color = mapOf(
        Pair(LogLevel.Verbose, 0xFF999999.toInt()),
        Pair(LogLevel.Debug, 0xFF19BB22.toInt()),
        Pair(LogLevel.Info, 0xFF0885BB.toInt()),
        Pair(LogLevel.Warn, 0xFFCCAE17.toInt()),
        Pair(LogLevel.Error, 0xFFE75552.toInt()),
        Pair(LogLevel.Assert, 0xFFFF840C.toInt()),
    )

    override fun getBaseColor(level: LogLevel): Int {
        return level2Color[level] ?: 0xFFBBBBBB.toInt()
    }
}