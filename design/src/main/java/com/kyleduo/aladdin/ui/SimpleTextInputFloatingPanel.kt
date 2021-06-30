package com.kyleduo.aladdin.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kyleduo.aladdin.api.manager.genie.AladdinPanelController
import com.kyleduo.aladdin.ui.databinding.AladdinDesignSimpleTextInputFloatingPanelBinding

/**
 * [FloatingPanel] implementation for input simple text. This useful when an [AladdinGenie]
 * needs to retrieve text input from user.
 *
 * @author kyleduo on 2021/6/30
 */
@Suppress("MemberVisibilityCanBePrivate", "KDocUnresolvedReference")
class SimpleTextInputFloatingPanel(
    panelController: AladdinPanelController,
    val onTextInputConfirmedListener: OnTextInputConfirmedListener
) : EditableFloatingPanel(panelController) {

    override val contentLayoutResId: Int = R.layout.aladdin_design_simple_text_input_floating_panel
    lateinit var binding: AladdinDesignSimpleTextInputFloatingPanelBinding


    override fun onContentInflated(content: View) {
        super.onContentInflated(content)
        binding = AladdinDesignSimpleTextInputFloatingPanelBinding.bind(content)
        binding.aladdinDesignSimpleTextInputInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm: InputMethodManager =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(v, 0)
            }
        }
    }

    fun show(text: String, title: String) {
        super.show()
        binding.aladdinDesignSimpleTextInputTitle.text = title
        binding.aladdinDesignSimpleTextInputInput.setText(text)
    }

    override fun onShow() {
        super.onShow()
        binding.aladdinDesignSimpleTextInputInput.postDelayed({
            binding.aladdinDesignSimpleTextInputInput.requestFocus()
        }, 100)
    }

    override fun onDismiss() {
        super.onDismiss()
        onTextInputConfirmedListener.onTextInputConfirmed(binding.aladdinDesignSimpleTextInputInput.text.toString())
    }

    interface OnTextInputConfirmedListener {

        fun onTextInputConfirmed(text: String)
    }
}
