package com.kyleduo.aladdin.genie.hook

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.genie.hook.data.ActionGroupItem
import com.kyleduo.aladdin.genie.hook.data.HookAction
import com.kyleduo.aladdin.genie.hook.data.NoReceiverAction
import com.kyleduo.aladdin.genie.hook.data.NoReceiverRefHolder
import com.kyleduo.aladdin.genie.hook.databinding.AladdinGenieHookPanelBinding
import com.kyleduo.aladdin.genie.hook.view.HookActionGroupItemViewDelegate
import com.kyleduo.aladdin.genie.hook.view.HookActionItemViewDelegate
import com.kyleduo.aladdin.ui.OnItemClickListener
import com.kyleduo.aladdin.ui.SimpleOffsetDecoration
import com.kyleduo.aladdin.ui.dp2px
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*

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
class HookGenie : AladdinViewGenie(), OnReferenceRecycledListener,
    OnItemClickListener<HookAction<Any>> {
    companion object {
        const val KEY = "aladdin-genie-hook"

        private const val TAG = "HookGenie"
        private const val DEFAULT_GROUP_NAME = "--"
    }

    override val title: String = "Hook"
    override val key: String = KEY

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

    /**
     * receiver's class -> instance history [hash]
     */
    private val receiverCounter: MutableMap<Class<*>, MutableList<Int>> = mutableMapOf()

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.register(
                ActionGroupItem::class.java,
                HookActionGroupItemViewDelegate(object : OnItemClickListener<ActionGroupItem> {
                    override fun onItemClick(position: Int, item: ActionGroupItem) {
                        onGroupItemClicked(item)
                    }
                })
            )
            it.register(HookAction::class.java, HookActionItemViewDelegate(this))
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
                    it.addItemDecoration(SimpleOffsetDecoration(8.dp2px(), 8.dp2px()))
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

    fun onGroupItemClicked(group: ActionGroupItem) {
        val list = adapter.items.toMutableList()
        val index = list.indexOf(group)
        val newGroupItem = group.copy(isFold = !group.isFold)
        if (group.isFold) {
            // unfold
            val actions = actionsMap.values.flatten().filter { it.group == group.name }.toList()
            list.addAll(index + 1, actions)
        } else {
            // fold
            val toBeRemoved = mutableListOf<HookAction<*>>()
            for (i in index + 1 until list.size) {
                val item = list[i]
                (item as? HookAction<*>)?.let {
                    if (it.group == group.name) {
                        toBeRemoved.add(it)
                    }
                }
            }
            list.removeAll(toBeRemoved)
        }
        list[index] = newGroupItem
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    fun register(
        key: String,
        title: String,
        group: String,
        action: NoReceiverAction
    ) {
        register(key, title, group, NoReceiverRefHolder) {
            action()
        }
    }

    fun <R : Any> register(
        key: String,
        title: String,
        group: String,
        receiver: R,
        action: (receiver: R) -> Unit
    ) {
        val ref = getReference(receiver)
        val hash = receiver.hashCode()

        var actions = actionsMap[ref]
        if (actions == null) {
            actions = mutableListOf()
            actionsMap[ref] = actions
        }

        if (actions.find { it.key == key } != null) {
            Log.w(TAG, "action key '$key' conflict.")
            return
        }

        val receiverClass = receiver::class.java
        val records = receiverCounter[receiverClass] ?: mutableListOf<Int>().also {
            receiverCounter[receiverClass] = it
        }

        val index = if (records.contains(hash)) {
            records.indexOf(hash) + 1
        } else {
            records.add(hash)
            records.size
        }

        val groupName = if (group.isEmpty()) DEFAULT_GROUP_NAME else group

        val hookAction = HookAction(
            key,
            title,
            groupName,
            receiverClass,
            index,
            ref,
            action
        )

        actions.add(hookAction)

        refreshActionsList()
    }

    fun unregister(key: String, receiver: Any) {
        val ref = getReference(receiver, false) ?: return

        val actions = actionsMap[ref] ?: return
        actions.removeAll { it.key == key }

        refreshActionsList()
    }

    private fun <R : Any> getReference(instance: R): WeakReference<R> {
        return getReference(instance, create = true)!!
    }

    private fun <R : Any> getReference(instance: R, create: Boolean): WeakReference<R>? {
        val hash = instance.hashCode()
        val exists = referenceMap[hash]
        @Suppress("UNCHECKED_CAST")
        if (exists != null) {
            return exists as WeakReference<R>
        }
        if (!create) {
            return null
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
        val originList = adapter.items

        val groups = mutableMapOf<String, MutableList<HookAction<*>>>()
        val actions = actionsMap.values.flatten().toList()
        actions.forEach {
            val group = groups[it.group] ?: mutableListOf<HookAction<*>>().also { list ->
                groups[it.group] = list
            }
            group.add(it)
        }

        val groupNames = groups.keys.sorted()

        val foldByDefault = actions.size > 10
        val viewItems = mutableListOf<Any>()
        groupNames.forEach {
            val name = it
            val groupActions = groups[it] ?: listOf<Any>()
            val originIsFold = (originList.find { item ->
                (item as? ActionGroupItem)?.name == name
            } as? ActionGroupItem)?.isFold ?: foldByDefault
            val group = ActionGroupItem(
                name,
                groupActions.size,
                originIsFold
            )
            viewItems.add(group)
            if (!group.isFold) {
                viewItems.addAll(groupActions)
            }
        }

        adapter.items = viewItems
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int, item: HookAction<Any>) {
        val ref = item.reference.get()
        if (ref == null) {
            scanDecayedReference()
        } else {
            item.action(ref)
        }
    }
}