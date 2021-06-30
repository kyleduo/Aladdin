package com.kyleduo.aladdin.genies.board

import android.view.ViewGroup
import com.kyleduo.aladdin.api.manager.genie.AladdinPanelController
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie

/**
 * Implementation of [AladdinPanelController] with [AladdinBoard].
 *
 * @author kyleduo on 2021/6/22
 */
class BoardPanelController(
    override val panelContainer: ViewGroup,
    val board: AladdinBoard
) : AladdinPanelController {

    override var isSoftInputEnabled: Boolean
        get() = board.agent.isSoftInputEnabled
        set(value) {
            board.agent.isSoftInputEnabled = value
        }

    override fun hide() {
        board.hide()
    }

    override fun isSelected(genie: AladdinViewGenie): Boolean {
        return board.selectedGenie == genie
    }
}