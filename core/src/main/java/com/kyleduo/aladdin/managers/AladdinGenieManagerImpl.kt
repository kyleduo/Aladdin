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

    private val genies = mutableMapOf<Class<*>, MutableMap<String, AladdinGenie>>()

    init {
        addGenie(EntryGenie())
        addGenie(BoardGenie())

        configurator?.genies?.forEach {
            addGenie(it)
        }
    }

    override fun attach() {
        genies.forEach { entry ->
            entry.value.forEach {
                it.value.context = context
            }
        }
    }

    override fun ready() {
        genies.forEach { entry ->
            entry.value.forEach {
                it.value.onStart()
            }
        }
    }

    private fun addGenie(genie: AladdinGenie) {
        val map = genies[genie.apiClass] ?: mutableMapOf<String, AladdinGenie>().also {
            genies[genie.apiClass] = it
        }
        if (map.containsValue(genie)) {
            throw IllegalStateException("Genie has been added. [${genie.apiClass.canonicalName}] ")
        }
        val realKey = genie.key
        if (!genie.isMultipleSupported) {
            if (map.isNotEmpty()) {
                throw IllegalStateException("Multiple instance is not supported for this Genie. [${genie.apiClass.canonicalName}] ")
            }
        } else {
            if (map.containsKey(realKey)) {
                throw IllegalStateException("The key of Genie is conflicted. [key: ${genie.key}] [${genie.apiClass.canonicalName}] ")
            }
        }
        map[realKey] = genie
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> findGenie(clz: Class<T>, key: String?): T? {
        return genies[clz]?.let {
            it[key ?: AladdinGenie.DEFAULT_KEY]
        } as? T
    }

    override fun allGenies(): List<AladdinGenie> {
        return genies.flatMap { it.value.values }.toList()
    }
}
