package com.kyleduo.aladdin.genie.hook

import java.lang.ref.WeakReference

/**
 * @author kyleduo on 2021/6/17
 */
class HookAction<R : Any>(
    val key: String,
    val title: String,
    val reference: WeakReference<R>,
    val action: ReceiverAction<R>
)