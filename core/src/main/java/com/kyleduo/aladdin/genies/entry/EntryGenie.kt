package com.kyleduo.aladdin.genies.entry

import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

/**
 * @author kyleduo on 2021/5/28
 */
class EntryGenie : AladdinGenie() {

    private val entry by lazy { AladdinEntry(context) }
    override val key: String = "entry"

    override fun onStart() {
        context.viewManager.register(entry)
    }

    override fun onStop() {
    }

    fun show() {
        entry.show()
    }
}
