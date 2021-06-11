package com.kyleduo.aladdin.genie.appinfo.data

/**
 * Represents a group of app info data
 * @author kyleduo on 2021/6/11
 */
data class AppInfoSection(
    val title: String,
    val desc: String,
    val items: List<AppInfoItem>
)

/**
 * Represents a single data of application information.
 * @author kyleduo on 2021/6/11
 */
data class AppInfoItem(
    val name: String,
    val value: String
)
