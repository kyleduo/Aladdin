package com.kyleduo.aladdin.genie.hook.data

import java.lang.ref.WeakReference

/**
 * @author kyleduo on 2021/6/17
 */
class HookAction<R : Any>(
    val key: String,
    val title: String,
    val group: String,
    val reference: WeakReference<R>,
    val action: (receiver: R) -> Unit
)