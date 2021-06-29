package com.kyleduo.aladdin.api.manager.genie

import com.kyleduo.aladdin.api.manager.AladdinManager

/**
 * @author kyleduo on 2021/6/12
 */
interface AladdinGenieManager : AladdinManager {

    fun findGenie(key: String): AladdinGenie?

    fun allGenies(): List<AladdinGenie>
}