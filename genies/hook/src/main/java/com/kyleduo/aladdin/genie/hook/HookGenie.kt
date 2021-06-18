package com.kyleduo.aladdin.genie.hook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.hook.data.HookAction
import com.kyleduo.aladdin.genie.hook.databinding.AladdinGenieHookPanelBinding
import com.kyleduo.aladdin.genie.hook.view.HookActionItemViewDelegate
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
class HookGenie : AladdinViewGenie(), OnReferenceRecycledListener {
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
     * receiver's hashCode -> reference
     */
    private val referenceMap: MutableMap<Int, WeakReference<*>> = mutableMapOf()

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.register(HookAction::class.java, HookActionItemViewDelegate())
        }
    }

    override fun createPanel(container: ViewGroup): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.aladdin_genie_hook_panel, container, false).also { view ->
                binding = AladdinGenieHookPanelBinding.bind(view)
                binding.aladdinGenieHookActionsList.also {
                    it.adapter = this.adapter
                    it.layoutManager =
                        LinearLayoutManager(context.app, LinearLayoutManager.VERTICAL, false)
                }
            }
    }

    override fun onSelected() {
        scanDecayedReference()
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
        group: String,
        receiver: R,
        action: (receiver: R) -> Unit
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
            group,
            ref,
            action
        )

        actions.add(hookAction)

        refreshActionsList()
    }

    private fun <R : Any> getReference(instance: R): WeakReference<R> {
        val hash = instance.hashCode()
        val exists = referenceMap[hash]
        @Suppress("UNCHECKED_CAST")
        if (exists != null) {
            return exists as WeakReference<R>
        }
        return WeakReference(instance, referenceTracker.referenceQueue).also {
            referenceMap[hash] = it
        }
    }

    override fun onReferenceRecycled(reference: Reference<*>) {
        discardReference(reference)

        refreshActionsList()
    }

    private fun discardReference(reference: Reference<*>) {
        val entry = referenceMap.entries.find { it.value == reference } ?: return
        referenceMap.remove(entry.key)
        actionsMap.remove(reference)
    }

    private fun scanDecayedReference() {
        val decayedReferences = referenceMap.values.filter { it.get() == null }.toList()
        decayedReferences.forEach {
            discardReference(it)
        }

        refreshActionsList()
    }

    private fun refreshActionsList() {
        val actions = actionsMap.values.flatten().toList()
        adapter.items = actions
        adapter.notifyDataSetChanged()
    }
}