package com.kyleduo.aladdin.ui

import android.view.View
import android.view.ViewGroup
import com.kyleduo.aladdin.ui.databinding.AladdinDesignFloatingPanelBinding

/**
 * @author kyleduo on 2021/6/29
 */
abstract class FloatingPanel(
    @Suppress("MemberVisibilityCanBePrivate") val container: ViewGroup
) {

    @Suppress("MemberVisibilityCanBePrivate")
    protected val rootBinding by lazy {
        AladdinDesignFloatingPanelBinding.inflate(
            container.context.layoutInflater(),
            container,
            false
        ).also {
            it.aladdinDesignFloatingPanelClose.setOnClickListener {
                dismiss()
            }
            val content =
                it.aladdinDesignFloatingPanelContentContainer.inflateView(contentLayoutResId)
            it.aladdinDesignFloatingPanelContentContainer.addView(content)
            onContentInflated(content)
        }
    }

    abstract val contentLayoutResId: Int

    fun show() {
        removeFromSuper()
        container.addView(rootBinding.root)

        onShow()
    }

    fun dismiss() {
        removeFromSuper()

        onDismiss()
    }

    private fun removeFromSuper() {
        val root = rootBinding.root
        (root.parent as? ViewGroup)?.removeView(root)
    }

    open fun onContentInflated(content: View) {

    }

    open fun onShow() {}

    open fun onDismiss() {}
}