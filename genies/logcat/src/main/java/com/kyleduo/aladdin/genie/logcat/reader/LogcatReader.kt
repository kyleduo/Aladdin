package com.kyleduo.aladdin.genie.logcat.reader

import android.os.Handler
import android.os.Looper
import com.kyleduo.aladdin.genie.logcat.LogcatGenie
import com.kyleduo.aladdin.genie.logcat.data.LogItem

/**
 * @author kyleduo on 3/20/21
 */
class LogcatReader(
    private val parser: LogcatParser,
    private val callback: OnLogItemListener
) : Thread() {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun run() {
        val proc = Runtime.getRuntime().exec("logcat")

        val source = proc.inputStream.bufferedReader()

        while (!isInterrupted) {
            val line = source.readLine()
            line?.let {
                if (it.contains(LogcatGenie.TAG)) {
                    return@let
                }
                val item = parser.parse(line)
                mainHandler.post {
                    callback.onLogItem(item)
                }
            }
        }
    }
}

interface OnLogItemListener {
    fun onLogItem(item: LogItem)
}
