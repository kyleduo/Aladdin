package com.kyleduo.aladdin.genie.appinfo.view

/**
 * section title data
 * @author kyleduo on 2021/6/11
 */
data class AppInfoSectionTitle(
    val title: String,
    val desc: String
)

/**
 * section item data
 * @author kyleduo on 2021/6/11
 */
data class AppInfoItem(
    val name: String,
    val value: String
)