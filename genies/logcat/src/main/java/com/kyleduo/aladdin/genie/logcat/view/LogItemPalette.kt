package com.kyleduo.aladdin.genie.logcat.view

import com.kyleduo.aladdin.genie.logcat.data.LogLevel

/**
 * @author kyleduo on 2021/6/13
 */
interface LogItemPalette {

    fun getBaseColor(level: LogLevel): Int
}