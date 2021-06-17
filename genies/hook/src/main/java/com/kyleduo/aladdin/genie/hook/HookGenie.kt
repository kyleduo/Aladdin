package com.kyleduo.aladdin.genie.hook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.hook.databinding.AladdinGenieHookPanelBinding
import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * HookGenie is useful when you need to outlet a trigger of an action in runtime. For example, if
 * you want to trigger some logic which is hard to simulate, you can expose an action to [HookGenie]
 * and you can trigger it through [HookGenie].
 *
 * [HookGenie] will track the reference of the receiver of actions so it's safe to use it with
 * Activity or other instance that has lifecycle.
 *
 * @author kyleduo on 2021/6/17
 */
class HookGenie(context: AladdinContext) : AladdinViewGenie(context), OnReferenceRecycledListener {
    override val title: String = "Hook"
    override val key: String = "aladdin-genie-hook"

    private lateinit var binding: AladdinGenieHookPanelBinding

    private val referenceTracker = ReferenceTracker(this)

    /**
     * reference -> list of actions
     */
    private val actionsMap: MutableMap<WeakReference<*>, MutableList<HookAction<*>>> =
        mutableMapOf()

    /**
     * receiver's class -> reference
     */
    private val referenceMap: MutableMap<Class<*>, WeakReference<*>> = mutableMapOf()

    override fun createPanel(container: ViewGroup): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.aladdin_genie_hook_panel, container, false).also {
                binding = AladdinGenieHookPanelBinding.bind(it)
            }
    }

    override fun onSelected() {
    }

    override fun onDeselected() {
    }

    override fun onStart() {
        referenceTracker.start()
    }

    override fun onStop() {
        referenceTracker.stop()
    }

    fun <R : Any> register(
        key: String,
        title: String,
        receiver: R,
        action: ReceiverAction<R>
    ) {
        val ref = getReference(receiver)

        var actions = actionsMap[ref]
        if (actions == null) {
            actions = mutableListOf()
            actionsMap[ref] = actions
        }

        val hookAction = HookAction(
            key,
            title,
            ref,
            action
        )

        actions.add(hookAction)
    }

    private fun <R : Any> getReference(instance: R): WeakReference<R> {
        val exists = referenceMap[instance::class.java]
        @Suppress("UNCHECKED_CAST")
        if (exists != null) {
            return exists as WeakReference<R>
        }
        return WeakReference(instance, referenceTracker.referenceQueue).also {
            referenceMap[instance::class.java] = it
        }
    }

    override fun onReferenceRecycled(reference: Reference<*>) {
        val entry = referenceMap.entries.find { it.value == reference } ?: return
        referenceMap.remove(entry.key)

        actionsMap.remove(reference)
    }
}