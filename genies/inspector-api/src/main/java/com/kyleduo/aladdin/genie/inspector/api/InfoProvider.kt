package com.kyleduo.aladdin.genie.inspector.api

import com.kyleduo.aladdin.genie.inspector.api.data.InfoSection

/**
 * Providing app info to [InspectorGenie]
 * @author kyleduo on 2021/6/11
 */
interface InfoProvider {

    fun provideInfo(): InfoSection?
}
