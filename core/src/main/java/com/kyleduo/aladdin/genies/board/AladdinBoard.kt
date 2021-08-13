package com.kyleduo.aladdin.genies.board

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyleduo.aladdin.R
import com.kyleduo.aladdin.api.Aladdin
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.genie.AladdinGenie
import com.kyleduo.aladdin.api.manager.genie.AladdinViewGenie
import com.kyleduo.aladdin.api.manager.view.AladdinView
import com.kyleduo.aladdin.genies.entry.EntryGenie
import com.kyleduo.aladdin.ui.dp2px

/**
 * @author kyleduo on 2021/5/31
 */
class AladdinBoard(val context: AladdinContext) : AladdinView(), OnTabSelectedListener {

    override val view: ConstraintLayout by lazy {
        val root = LayoutInflater.from(context.app)
            .inflate(
                R.layout.aladdin_board,
                FrameLayout(context.app),
                false
            ) as ConstraintLayout
        root.also {
            it.setOnClickListener {
                hide()
            }
        }
    }
    override val tag: String = "Board"
    private val contentView by lazy {
        view.findViewById<LinearLayout>(R.id.aladdin_boardContent).also { it.clipToOutline = true }
    }
    private val tabAdapter = TabAdapter(this)
    private val tabList by lazy {
        contentView.findViewById<RecyclerView>(R.id.aladdin_boardTabList).also {
            it.adapter = tabAdapter
            it.layoutManager =
                LinearLayoutManager(it.context, LinearLayoutManager.HORIZONTAL, false)
            it.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position =
                        (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition

                    val margin = 8.dp2px()
                    if (position == 0) {
                        outRect.set(margin, margin, margin, margin)
                    } else {
                        outRect.set(0, margin, margin, margin)
                    }
                }
            })
        }
    }
    private val panelContainer by lazy { contentView.findViewById<FrameLayout>(R.id.aladdin_boardGeniePanelContainer) }
    internal var selectedGenie: AladdinViewGenie? = null
        set(value) {
            if (field == value) {
                return
            }
            val before = field
            lastSelectedGenie = before
            before?.onDeselected()
            field = value
            value?.onSelected()

            tabAdapter.selectedGenie = value

            onGenieSelected(before, value)
        }
    private var lastSelectedGenie: AladdinViewGenie? = null

    private val genies = LinkedHashMap<String, AladdinViewGenie>()

    override fun onAgentBound() {
        super.onAgentBound()
        agent.resize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun show() {
        agent.show()

        if (genies.isNotEmpty() && selectedGenie == null) {
            selectedGenie = lastSelectedGenie ?: genies.values.first()
        }
    }

    private fun prepareGenieView() {
        for (genie in this.genies.values) {
            attachGenie(genie)
        }

        // init
        tabList

        tabAdapter.genies = this.genies.values.toList()
    }

    private fun attachGenie(genie: AladdinViewGenie) {
        val geniePanelContainer = FrameLayout(context.app).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.tag = genie.panelTag()
            it.visibility = View.GONE
            // Perhaps there's an EditText in a panel, this property can make sure that EditText
            // lose focus while other panel is selected, and the EditText not request focus
            // atomically after this panel is selected.
            it.isFocusableInTouchMode = true
        }
        genie.panelController = BoardPanelController(geniePanelContainer, this)
        geniePanelContainer.addView(genie.createPanel(geniePanelContainer))

        panelContainer.addView(geniePanelContainer)
    }

    private fun onGenieSelected(before: AladdinViewGenie?, after: AladdinViewGenie?) {
        before?.let {
            panelContainer.findViewWithTag<View>(it.panelTag()).isVisible = false
        }

        after?.let {
            panelContainer.findViewWithTag<View>(it.panelTag()).isVisible = true
        }
    }

    fun addGenies(genies: List<AladdinGenie>) {
        for (genie in genies) {
            if (genie is AladdinViewGenie) {
                this.genies[genie.key()] = genie
            }
        }

        prepareGenieView()
    }

    fun hide() {
        selectedGenie = null
        agent.dismiss()
        Aladdin.findGenie(EntryGenie::class.java)?.show()
    }

    override fun onTabSelected(position: Int, genie: AladdinViewGenie) {
        selectedGenie = genie
    }

    private fun AladdinViewGenie.key() = apiClass.canonicalName ?: apiClass.name
    private fun AladdinViewGenie.panelTag() = "panel_${key()}"
}
