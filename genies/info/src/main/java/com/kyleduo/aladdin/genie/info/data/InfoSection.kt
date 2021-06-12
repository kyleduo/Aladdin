package com.kyleduo.aladdin.genie.info.data

/**
 * Represents a group of app info data
 *
 * @author kyleduo on 2021/6/11
 */
data class InfoSection(
    val title: String,
    val desc: String,
    val items: List<InfoItem>
)

/**
 * Represents a single data of application information.
 *
 * @author kyleduo on 2021/6/11
 */
data class InfoItem(
    val name: String,
    val value: String
)
