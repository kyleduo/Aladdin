package com.kyleduo.aladdin.genies

import android.view.View

/**
 * Abstract [IGenie] definition for Genie with user interface, probably a panel.
 *
 * @author kyleduo on 2021/5/25
 */
abstract class ViewGenie : IGenie {

    abstract val panel: View

    /**
     * title for this genie, which will be used for the panel
     */
    abstract val title: String

    /**
     * Lifecycle callback invoked after the [panel] has been selected.
     */
    abstract fun onSelected()

    /**
     * Lifecycle callback invoked after the [panel] has been deselected.
     */
    abstract fun onDeselected()
}
