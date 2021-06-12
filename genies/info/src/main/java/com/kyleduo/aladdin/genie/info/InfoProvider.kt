package com.kyleduo.aladdin.genie.info

import com.kyleduo.aladdin.genie.info.data.InfoSection

/**
 * Providing app info to [AppInfoGenie]
 * @author kyleduo on 2021/6/11
 */
interface InfoProvider {

    fun provideAppInfo(): InfoSection?
}
