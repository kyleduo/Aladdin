package com.kyleduo.aladdin.genie.hook

import android.os.Handler
import android.os.Looper
import androidx.annotation.WorkerThread
import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue

/**
 * @author kyleduo on 2021/6/17
 */
class ReferenceTracker(
    private val referenceRecycledListener: OnReferenceRecycledListener
) : Runnable {

    private val mainHandler = Handler(Looper.getMainLooper())

    private var thread: Thread? = null

    val referenceQueue: ReferenceQueue<Any> = ReferenceQueue()

    fun start() {
        thread = Thread(this, "ReceiverTracker").also {
            it.start()
        }
    }

    fun stop() {
        thread?.interrupt()
        thread = null
    }

    @WorkerThread
    override fun run() {
        while (thread?.isInterrupted == false) {
            var ref: Reference<*>?
            while (referenceQueue.remove().also { ref = it } != null) {
                mainHandler.post {
                    ref?.let {
                        referenceRecycledListener.onReferenceRecycled(it)
                    }
                }
            }
        }
    }
}