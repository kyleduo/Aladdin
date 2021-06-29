package com.kyleduo.aladdin.genies.board

import android.view.ViewGroup
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.api.manager.genie.AladdinPanelController

/**
 * Implementation of [AladdinPanelController] with [AladdinBoard].
 *
 * @author kyleduo on 2021/6/22
 */
class BoardPanelController(
    override val panelContainer: ViewGroup,
    val board: AladdinBoard
) : AladdinPanelController {

    override fun hide() {
        board.hide()
    }

    override fun isSelected(genie: AladdinViewGenie): Boolean {
        return board.selectedGenie == genie
    }
}