package com.kyleduo.aladdin.ui

/**
 * @author kyleduo on 2021/6/23
 */
interface OnItemClickListener<ItemType : Any> {

    fun onItemClick(position: Int, item: ItemType)
}