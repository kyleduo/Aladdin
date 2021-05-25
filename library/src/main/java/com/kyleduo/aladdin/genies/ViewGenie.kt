package com.kyleduo.aladdin.genies

import android.view.View
import android.view.ViewGroup

/**
 * Abstract [IGenie] definition for Genie with user interface, probably a panel.
 *
 * @author kyleduo on 2021/5/25
 */
abstract class ViewGenie(
    protected val container: ViewGroup
) : IGenie {

    abstract val panel: View

    /**
     * Lifecycle callback invoked after the [panel] has been selected.
     */
    abstract fun onSelected()

    /**
     * Lifecycle callback invoked after the [panel] has been deselected.
     */
    abstract fun onDeselected()
}
