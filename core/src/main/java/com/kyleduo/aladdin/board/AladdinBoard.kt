package com.kyleduo.aladdin.board

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
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.R
import com.kyleduo.aladdin.entry.EntryGenie
import com.kyleduo.aladdin.genies.IGenie
import com.kyleduo.aladdin.genies.ViewGenie
import com.kyleduo.aladdin.utils.app
import com.kyleduo.aladdin.utils.dp2px
import com.kyleduo.aladdin.view.IAladdinView

/**
 * @author kyleduo on 2021/5/31
 */
class AladdinBoard : IAladdinView(), OnTabSelectedListener {

    override val view: ConstraintLayout by lazy {
        val root = LayoutInflater.from(Aladdin.app)
            .inflate(R.layout.aladdin_board, FrameLayout(Aladdin.app), false) as ConstraintLayout
        root.also {
            it.setOnClickListener {
                agent.dismiss()
                (Aladdin.genie("entry") as? EntryGenie)?.show()
            }
        }
    }
    override val tag: Any = "Board"
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
    private var selectedGenie: ViewGenie? = null
        set(value) {
            if (field == value) {
                return
            }
            val before = field
            field?.onDeselected()
            field = value
            value?.onSelected()

            tabAdapter.selectedGenie = value

            onGenieSelected(before, value)
        }

    private val genies = LinkedHashMap<String, ViewGenie>()

    override fun onAgentBound() {
        super.onAgentBound()
        agent.resize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun show() {
        agent.show()

        if (genies.isNotEmpty() && selectedGenie == null) {
            selectedGenie = genies.values.first()
        }
    }

    private fun prepareGenieView() {
        for (genie in this.genies.values) {
            genie.createPanel().also {
                panelContainer.addView(it)
            }
        }

        // init
        tabList

        tabAdapter.genies = this.genies.values.toList()
    }

//    private fun ViewGenie.createTab(container: ViewGroup): View {
//        val tab = LayoutInflater.from(container.context)
//            .inflate(R.layout.aladdin_board_tab, container, false) as TextView
//        return tab.also {
//            it.text = this.title
//            it.tag = tabTag()
//            it.gravity = Gravity.CENTER
//            val genie = this
//            it.setOnClickListener {
//                Toast.makeText(Aladdin.app, "click ${this.key}", Toast.LENGTH_SHORT).show()
//                selectedGenie = genie
//            }
//        }
//    }

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
            panelContainer.findViewWithTag<View>(it.panelTag()).isVisible = false
        }

        after?.let {
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

    override fun onTabSelected(position: Int, genie: ViewGenie) {
        selectedGenie = genie
    }

    private fun ViewGenie.panelTag() = "panel_$key"
}
