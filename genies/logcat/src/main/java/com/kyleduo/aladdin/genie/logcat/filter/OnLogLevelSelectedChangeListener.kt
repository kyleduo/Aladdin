package com.kyleduo.aladdin.genie.logcat.filter

import com.kyleduo.aladdin.genie.logcat.data.LogLevel

/**
 * @author kyleduo on 2021/6/23
 */
interface OnLogLevelSelectedChangeListener {

    fun onLogLevelSelectedChanged(level: LogLevel, isSelected: Boolean)
}