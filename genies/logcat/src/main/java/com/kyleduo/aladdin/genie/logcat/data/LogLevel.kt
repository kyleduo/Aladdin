package com.kyleduo.aladdin.genie.logcat.data

/**
 * @author kyleduo on 2021/6/13
 */
enum class LogLevel(
    val badge: String,
    val level: Int,
) {
    Verbose("V", 1),
    Debug("D", 2),
    Info("I", 3),
    Warn("W", 4),
    Error("E", 5),
    Assert("A", 6);

    companion object {

        val levels = listOf(
            Verbose,
            Debug,
            Info,
            Warn,
            Error,
            Assert
        )

        fun parse(level: String): LogLevel {
            return values().find { it.badge == level } ?: Error
        }
    }
}