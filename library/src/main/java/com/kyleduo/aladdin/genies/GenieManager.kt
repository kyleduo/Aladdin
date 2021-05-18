package com.kyleduo.aladdin.genies

import com.kyleduo.aladdin.structure.IAladdinManager

/**
 * @author kyleduo on 2021/5/18
 */
class GenieManager : IAladdinManager {

    private val genies = mutableMapOf<String, IGenie>()

    override fun attach() {
    }

    fun addGenie(genie: IGenie) {
        if (genies[genie.key] != null) {
            throw IllegalStateException("Genie key conflicted. [${genie.key}] ")
        }
        genies[genie.key] = genie
    }

    fun findGenie(key: String): IGenie? {
        return genies[key]
    }
}
