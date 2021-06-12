package com.kyleduo.aladdin.managers

import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.config.GenieConfigurator
import com.kyleduo.aladdin.api.manager.genie.AladdinGenie
import com.kyleduo.aladdin.api.manager.genie.GenieManager
import com.kyleduo.aladdin.genies.board.BoardGenie
import com.kyleduo.aladdin.genies.entry.EntryGenie

/**
 * @author kyleduo on 2021/5/18
 */
class GenieManagerImpl(context: AladdinContext, configurator: GenieConfigurator?) : GenieManager {

    private val genies = mutableMapOf<String, AladdinGenie>()

    init {
        addGenie(EntryGenie(context))
        addGenie(BoardGenie(context))

        configurator?.genieFactories?.forEach {
            addGenie(it.create(context))
        }
    }

    override fun attach() {

    }

    override fun ready() {
        genies.forEach {
            it.value.onStart()
        }
    }

    private fun addGenie(genie: AladdinGenie) {
        if (genies[genie.key] != null) {
            throw IllegalStateException("Genie key conflicted. [${genie.key}] ")
        }
        genies[genie.key] = genie
    }

    override fun findGenie(key: String): AladdinGenie? {
        return genies[key]
    }

    override fun allGenies(): List<AladdinGenie> {
        return genies.values.toList()
    }
}
