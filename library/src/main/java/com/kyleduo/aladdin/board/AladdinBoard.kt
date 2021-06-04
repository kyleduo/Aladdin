package com.kyleduo.aladdin.board

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
    companion object {
        private const val TAG_CONTENT = "content"
    }

    override val view: View by lazy {
        FrameLayout(Aladdin.app).apply {
            setBackgroundColor(0x60000000)
            if (findViewWithTag<View>(TAG_CONTENT) == null) {
                contentView = LayoutInflater.from(Aladdin.app)
                    .inflate(R.layout.aladdin_board_portrait, this, false) as LinearLayout
                addView(contentView)
                contentView.tag = TAG_CONTENT
            }
            setOnClickListener {
                agent.dismiss()
                (Aladdin.genie("entry") as? EntryGenie)?.show()
            }

            setUpForPortrait()
        }
    }
    override val tag: Any = "Board"
    private lateinit var contentView: LinearLayout

    private val genies = LinkedHashMap<String, ViewGenie>()

    override fun onAgentBound() {
        super.onAgentBound()
        agent.resize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun setUpForPortrait() {
    }

    private fun setUpForLandscape() {
    }

    fun show() {
        agent.show()
    }

    private fun prepareGenieView() {
        for (genie in this.genies.values) {
            TextView(Aladdin.app).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                text = genie.title
                tag = genie.key
                gravity = Gravity.CENTER
                setOnClickListener {
                    Toast.makeText(Aladdin.app, "click ${it.tag}", Toast.LENGTH_SHORT).show()
                }
            }.also {
                contentView.findViewById<LinearLayout>(R.id.aladdin_boardTabContainer).addView(it)
            }
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
}
