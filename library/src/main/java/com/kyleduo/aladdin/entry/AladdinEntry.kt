package com.kyleduo.aladdin.entry

import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.R
import com.kyleduo.aladdin.board.BoardGenie
import com.kyleduo.aladdin.utils.app
import com.kyleduo.aladdin.utils.dp2px
import com.kyleduo.aladdin.view.IAladdinView

/**
 * Interface for entry view.
 *
 * @author kyleduo on 2021/5/18
 */
class AladdinEntry : IAladdinView() {

    companion object {
        private const val ENTRY_SIZE_DP = 36
    }

    override val view: View by lazy {
        AppCompatImageView(Aladdin.app).apply {
            setImageResource(R.drawable.aladdin_entry_icon)

            setOnClickListener {
                Toast.makeText(Aladdin.app, "Click Entry !!!", Toast.LENGTH_SHORT).show()
//                val random = Random(System.currentTimeMillis())
//                agent.moveBy(random.nextInt(-100, 100), random.nextInt(-100, 100))
                agent.dismiss()
                (Aladdin.genie("board") as? BoardGenie)?.show()
            }
        }
    }
    override val tag: Any = "Entry"

    override fun onAgentBound() {
        super.onAgentBound()

        val entrySize = ENTRY_SIZE_DP.dp2px()
        agent.resize(entrySize, entrySize)
        agent.gravity(Gravity.CENTER_VERTICAL or Gravity.START)
        agent.show()
    }

    fun show() {
        agent.show()
    }
}
