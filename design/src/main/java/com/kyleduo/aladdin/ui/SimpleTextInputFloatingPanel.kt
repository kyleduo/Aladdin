package com.kyleduo.aladdin.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
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
) : EditableFloatingPanel(panelController), OnItemClickListener<String> {

    companion object {
        private const val SP_NAME = "aladdin_input_history"

        private const val HISTORY_PREFIX = "history_"
        private const val SEPARATER = "-.-|||"
    }

    override val contentLayoutResId: Int = R.layout.aladdin_design_simple_text_input_floating_panel
    lateinit var binding: AladdinDesignSimpleTextInputFloatingPanelBinding
    private var key: String = ""

    private val adapter by lazy {
        MultiTypeAdapter().also {
            it.register(String::class.java, StringListItemDelegate(this))
        }
    }

    private val sp by lazy {
        panelController.panelContainer.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

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
        binding.aladdinDesignSimpleTextInputHistoryList.apply {
            adapter = this@SimpleTextInputFloatingPanel.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        binding.aladdinDesignSimpleTextInputHistoryClear.setOnClickListener {
            saveHistory(listOf())
        }
        binding.aladdinDesignSimpleTextInputConfirm.setOnClickListener {
            transferInputResult()
            dismiss()
        }
    }

    private var historyItems = listOf<String>()

    fun show(key: String, title: String, defaultText: String = "") {
        super.show()
        this.key = key
        binding.aladdinDesignSimpleTextInputTitle.text = title
        binding.aladdinDesignSimpleTextInputInput.setText(defaultText)

        historyItems = getHistory()

        adapter.items = historyItems
        adapter.notifyDataSetChanged()
    }

    private val spKey: String
        get() = HISTORY_PREFIX + key

    private fun getHistory(): List<String> {
        val str = sp.getString(spKey, "")
        return if (str.isNullOrEmpty()) {
            listOf()
        } else {
            str.split(SEPARATER).toList()
        }
    }

    private fun saveHistory(history: List<String>) {
        if (history.isEmpty()) {
            sp.edit().remove(spKey).apply()
        } else {
            val str = history.joinToString(SEPARATER)
            sp.edit().putString(spKey, str).apply()
        }
        historyItems = history
        adapter.items = historyItems
        adapter.notifyDataSetChanged()
    }

    override fun onShow() {
        super.onShow()
        binding.aladdinDesignSimpleTextInputInput.postDelayed({
            binding.aladdinDesignSimpleTextInputInput.requestFocus()
        }, 100)
    }

    private fun transferInputResult() {
        val text = binding.aladdinDesignSimpleTextInputInput.text.toString()
        if (text.isNotEmpty() && text !in historyItems) {
            historyItems = historyItems.toMutableList().also { it.add(0, text) }
            saveHistory(historyItems)
        }
        onTextInputConfirmedListener.onTextInputConfirmed(text)
    }

    interface OnTextInputConfirmedListener {

        fun onTextInputConfirmed(text: String)
    }

    override fun onItemClick(position: Int, item: String) {
        binding.aladdinDesignSimpleTextInputInput.setText(item)
        binding.aladdinDesignSimpleTextInputInput.setSelection(item.length)
    }
}
