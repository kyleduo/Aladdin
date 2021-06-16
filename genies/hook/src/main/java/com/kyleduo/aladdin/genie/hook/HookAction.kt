package com.kyleduo.aladdin.genie.hook

/**
 * @author kyleduo on 2021/6/17
 */
class HookAction<R : Any>(
    val key: String,
    val title: String,
    val action: (R) -> Unit
)