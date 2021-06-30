package com.kyleduo.aladdin.ui

import com.kyleduo.aladdin.api.manager.genie.AladdinPanelController

/**
 * [FloatingPanel] with soft input supports.
 *
 * @author kyleduo on 2021/6/30
 */
abstract class EditableFloatingPanel(
    panelController: AladdinPanelController
) : FloatingPanel(panelController) {

    override fun onShow() {
        super.onShow()
        panelController.isSoftInputEnabled = true
    }

    override fun onDismiss() {
        super.onDismiss()
        panelController.isSoftInputEnabled = false
    }
}
