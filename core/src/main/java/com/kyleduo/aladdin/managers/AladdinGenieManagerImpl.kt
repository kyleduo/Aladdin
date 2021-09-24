package com.kyleduo.aladdin.managers

import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.config.AladdinGenieConfigurator
import com.kyleduo.aladdin.api.manager.genie.AladdinGenie
import com.kyleduo.aladdin.api.manager.genie.AladdinGenieManager
import com.kyleduo.aladdin.genies.board.BoardGenie
import com.kyleduo.aladdin.genies.entry.EntryGenie

/**
 * @author kyleduo on 2021/5/18
 */
class AladdinGenieManagerImpl(
    val context: AladdinContext,
    configurator: AladdinGenieConfigurator?
) : AladdinGenieManager {

    private val genies = mutableMapOf<Class<*>, AladdinGenie>()

    init {
        addGenie(EntryGenie())
        addGenie(BoardGenie())

        configurator?.genies?.forEach {
            addGenie(it)
        }
    }

    override fun attach() {
        genies.forEach {
            it.value.context = context
        }
    }

    override fun ready() {
        genies.forEach {
            it.value.onStart()
        }
    }

    private fun addGenie(genie: AladdinGenie) {
        if (genies[genie.apiClass] != null) {
            throw IllegalStateException("Genie conflicted. [${genie.apiClass.canonicalName}] ")
        }
        genies[genie.apiClass] = genie
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> findGenie(clz: Class<T>): T? {
        return genies[clz] as? T
    }

    override fun allGenies(): List<AladdinGenie> {
        return genies.values.toList()
    }
}
