package com.kyleduo.aladdin.genie.hook

/**
 * @author kyleduo on 2021/6/17
 */

typealias ReceiverAction<R> = (receiver: R) -> Unit
typealias NoReceiverAction = () -> Unit