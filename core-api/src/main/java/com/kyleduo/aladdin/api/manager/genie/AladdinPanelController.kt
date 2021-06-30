package com.kyleduo.aladdin.api.manager.genie

import android.view.ViewGroup

/**
 * Controller for [AladdinViewGenie] to access abilities provided by 'Board'
 *
 * @author kyleduo on 2021/6/22
 */
interface AladdinPanelController {

    /**
     * Container of a panel. Typically the view of panel is not just added to board, otherwise, it
     * was added to a panel of board.
     */
    val panelContainer: ViewGroup

    /**
     * Set whether soft input is enabled. This property takes effect in
     * [com.kyleduo.aladdin.api.manager.view.AladdinViewMode.Global] mode. By default, any view
     * in Global mode will not receive any soft keyboard input events.
     */
    var isSoftInputEnabled: Boolean

    /**
     * hide the panel (board)
     */
    fun hide()

    /**
     * whether this genie is currently selected
     */
    fun isSelected(genie: AladdinViewGenie): Boolean
}