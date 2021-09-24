package com.kyleduo.aladdin.api.manager.genie

import com.kyleduo.aladdin.api.manager.AladdinManager

/**
 * @author kyleduo on 2021/6/12
 */
interface AladdinGenieManager : AladdinManager {

    fun <T> findGenie(clz: Class<T>, key: String?): T?

    fun allGenies(): List<AladdinGenie>
}