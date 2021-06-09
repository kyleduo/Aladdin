package com.kyleduo.aladdin.board

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.R
import com.kyleduo.aladdin.entry.EntryGenie
import com.kyleduo.aladdin.genies.IGenie
import com.kyleduo.aladdin.genies.ViewGenie
import com.kyleduo.aladdin.utils.app
import com.kyleduo.aladdin.view.IAladdinView

/**
 * @author kyleduo on 2021/5/31
 */
class AladdinBoard : IAladdinView() {

    override val view: FrameLayout by lazy {
        (LayoutInflater.from(Aladdin.app)
            .inflate(R.layout.aladdin_board, FrameLayout(Aladdin.app), false)
                as FrameLayout).also {
            it.setOnClickListener {
                agent.dismiss()
                (Aladdin.genie("entry") as? EntryGenie)?.show()
            }
        }
    }
    override val tag: Any = "Board"
    private val contentView by lazy { view.findViewById<LinearLayout>(R.id.aladdin_boardContent) }
    private val tabContainer by lazy { contentView.findViewById<LinearLayout>(R.id.aladdin_boardTabContainer) }
    private val panelContainer by lazy { contentView.findViewById<FrameLayout>(R.id.aladdin_boardGeniePanelContainer) }
    private var selectedGenie: ViewGenie? = null
        set(value) {
            if (field == value) {
                return
            }
            val before = field
            field?.onDeselected()
            field = value
            value?.onSelected()

            onGenieSelected(before, value)
        }

    private val genies = LinkedHashMap<String, ViewGenie>()

    override fun onAgentBound() {
        super.onAgentBound()
        agent.resize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun setUpForPortrait() {
    }

    private fun setUpForLandscape() {
        TODO("need implementation")
    }

    fun show() {
        agent.show()

        if (genies.isNotEmpty() && selectedGenie == null) {
            selectedGenie = genies.values.first()
        }
    }

    private fun prepareGenieView() {
        for (genie in this.genies.values) {
            genie.createTab().also {
                tabContainer.addView(it)
            }
            genie.createPanel().also {
                panelContainer.addView(it)
            }
        }
    }

    private fun ViewGenie.createTab(): View {
        return TextView(Aladdin.app).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.text = this.title
            it.tag = tabTag()
            it.gravity = Gravity.CENTER
            val genie = this
            it.setOnClickListener {
                Toast.makeText(Aladdin.app, "click ${this.key}", Toast.LENGTH_SHORT).show()
                selectedGenie = genie
            }
        }
    }

    private fun ViewGenie.createPanel(): View {
        return FrameLayout(Aladdin.app).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.tag = panelTag()
            it.visibility = View.GONE
            it.addView(this.createPanel(it))
        }
    }

    private fun onGenieSelected(before: ViewGenie?, after: ViewGenie?) {
        before?.let {
            tabContainer.findViewWithTag<View>(it.tabTag()).isSelected = false
            panelContainer.findViewWithTag<View>(it.panelTag()).isVisible = false
        }

        after?.let {
            tabContainer.findViewWithTag<View>(it.tabTag()).isSelected = true
            panelContainer.findViewWithTag<View>(it.panelTag()).isVisible = true
        }
    }

    fun addGenies(genies: List<IGenie>) {
        for (genie in genies) {
            if (genie is ViewGenie) {
                this.genies[genie.key] = genie
            }
        }

        prepareGenieView()
    }

    private fun ViewGenie.tabTag() = "tab_$key"
    private fun ViewGenie.panelTag() = "panel_$key"
}
