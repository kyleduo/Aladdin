package com.kyleduo.aladdin.genies.entry

import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.kyleduo.aladdin.R
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.view.AladdinView
import com.kyleduo.aladdin.api.manager.view.SnapEdge
import com.kyleduo.aladdin.genies.board.BoardGenie
import com.kyleduo.aladdin.ui.dp2px

/**
 * Interface for entry view.
 *
 * @author kyleduo on 2021/5/18
 */
class AladdinEntry(context: AladdinContext) : AladdinView() {

    companion object {
        private const val ENTRY_SIZE_DP = 36
    }

    override val view: View by lazy {
        AppCompatImageView(context.app).apply {
            setImageResource(R.drawable.aladdin_entry_icon)

            setOnClickListener {
                Toast.makeText(context.app, "Click Entry !!!", Toast.LENGTH_SHORT).show()
//                val random = Random(System.currentTimeMillis())
//                agent.moveBy(random.nextInt(-100, 100), random.nextInt(-100, 100))
                agent.dismiss()
                (context.genieManager.findGenie("board") as? BoardGenie)?.show()
            }
        }
    }
    override val tag: String = "Entry"

    override val isDraggable: Boolean = true
    override val autoSnapEdges: Int = SnapEdge.LEFT or SnapEdge.RIGHT

    override fun onAgentBound() {
        super.onAgentBound()

        val entrySize = ENTRY_SIZE_DP.dp2px()
        agent.resize(entrySize, entrySize)
        agent.show()
    }

    fun show() {
        agent.show()
    }
}
