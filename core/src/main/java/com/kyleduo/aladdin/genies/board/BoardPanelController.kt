package com.kyleduo.aladdin.genies.board

import android.view.ViewGroup
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.api.manager.genie.PanelController

/**
 * Implementation of [PanelController] with [AladdinBoard].
 *
 * @author kyleduo on 2021/6/22
 */
class BoardPanelController(
    override val panelContainer: ViewGroup,
    val board: AladdinBoard
) : PanelController {

    override fun hide() {
        board.hide()
    }

    override fun isSelected(genie: AladdinViewGenie): Boolean {
        return board.selectedGenie == genie
    }
}