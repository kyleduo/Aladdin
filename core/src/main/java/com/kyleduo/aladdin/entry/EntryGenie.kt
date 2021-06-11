package com.kyleduo.aladdin.entry

import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.genies.IGenie

/**
 * @author kyleduo on 2021/5/28
 */
class EntryGenie : IGenie {

    private val entry = AladdinEntry()
    override val key: String = "entry"

    override fun onStart() {
        Aladdin.context.viewManager.register(entry)
    }

    override fun onStop() {
    }

    fun show() {
        entry.show()
    }
}
