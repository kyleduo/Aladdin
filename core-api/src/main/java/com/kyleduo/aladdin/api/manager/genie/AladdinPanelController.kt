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
     * hide the panel (board)
     */
    fun hide()

    /**
     * whether this genie is currently selected
     */
    fun isSelected(genie: AladdinViewGenie): Boolean
}