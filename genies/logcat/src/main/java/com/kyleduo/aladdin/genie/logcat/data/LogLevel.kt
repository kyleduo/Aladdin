package com.kyleduo.aladdin.genie.logcat.data

/**
 * @author kyleduo on 2021/6/13
 */
enum class LogLevel {
    Verbose,
    Debug,
    Info,
    Warn,
    Error,
    Assert;

    companion object {

        fun parse(level: String): LogLevel {
            return when (level) {
                "V" -> Verbose
                "D" -> Debug
                "I" -> Info
                "W" -> Warn
                "E" -> Error
                "A" -> Assert
                else -> Error
            }
        }
    }
}