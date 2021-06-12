package com.kyleduo.aladdin.api.manager.genie

import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.api.AladdinContext

/**
 * Abstract [AladdinGenie] definition for Genie with user interface, probably a panel.
 *
 * @author kyleduo on 2021/5/25
 */
abstract class AladdinViewGenie(context: AladdinContext) : AladdinGenie(context) {

    /**
     * title for this genie, which will be used for the panel
     */
    abstract val title: String

    /**
     * create panel view
     */
    abstract fun createPanel(container: ViewGroup): View

    /**
     * Lifecycle callback invoked after the panel has been selected.
     */
    abstract fun onSelected()

    /**
     * Lifecycle callback invoked after the panel has been deselected.
     */
    abstract fun onDeselected()
}
