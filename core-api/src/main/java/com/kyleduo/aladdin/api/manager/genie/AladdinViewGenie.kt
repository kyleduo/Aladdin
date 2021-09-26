package com.kyleduo.aladdin.api.manager.genie

import android.view.View
import android.view.ViewGroup

/**
 * Abstract [AladdinGenie] definition for Genie with user interface, probably a panel.
 *
 * @author kyleduo on 2021/5/25
 */
abstract class AladdinViewGenie(
    /**
     * title for this genie, which will be used for the panel
     */
    val title: String,
    key: String = DEFAULT_KEY
) : AladdinGenie(key) {

    /**
     * Inject before the view is added to board
     */
    lateinit var panelController: AladdinPanelController

    /**
     * create panel view
     */
    abstract fun createPanel(container: ViewGroup): View

    /**
     * Lifecycle callback invoked after the panel has been selected.
     */
    open fun onSelected() {}

    /**
     * Lifecycle callback invoked after the panel has been deselected.
     */
    open fun onDeselected() {}
}
