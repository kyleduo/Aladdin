package com.kyleduo.aladdin.genie.actions.api

/**
 * ActionsGenie is useful when you need to outlet a trigger of an action in runtime. For example, if
 * you want to trigger some logic which is hard to simulate, you can expose an action to [ActionsGenie]
 * and you can trigger it through [ActionsGenie].
 *
 * [ActionsGenie] will track the reference of the receiver of actions so it's safe to use it with
 * Activity or other instance that has lifecycle.
 *
 * @author kyleduo on 2021/6/29
 */
interface ActionsGenie {

    /**
     * Register an action without receiver.
     */
    fun register(key: String, title: String, group: String, action: () -> Unit)

    /**
     * Register an action with receiver.
     */
    fun <R : Any> register(
        key: String,
        title: String,
        group: String,
        receiver: R,
        action: R.() -> Unit
    )

    /**
     * Unregister an action with [key] as it's key.
     */
    fun unregister(key: String)

    /**
     * Discard actions bound with receiver.
     */
    fun discard(receiver: Any)
}
