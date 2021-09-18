package com.kyleduo.aladdin.genie.actions.data

import java.lang.ref.WeakReference

/**
 * @author kyleduo on 2021/6/17
 */
class Action<R : Any>(
    val key: String,
    val title: String,
    val group: String,
    val receiverClass: Class<*>,
    val receiverNo: Int,
    val reference: WeakReference<R>,
    val action: R.() -> Unit
)