package com.kyleduo.aladdin.entry

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.board.BoardGenie
import com.kyleduo.aladdin.utils.app
import com.kyleduo.aladdin.view.IAladdinView

/**
 * Interface for entry view.
 *
 * @author kyleduo on 2021/5/18
 */
class AladdinEntry : IAladdinView() {

    override val view: View by lazy {
        RelativeLayout(Aladdin.app).apply {
            setBackgroundColor(Color.CYAN and 0x40FFFFFF)
            minimumWidth = 100
            minimumHeight = 100

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

        agent.show()
        agent.gravity(Gravity.CENTER_VERTICAL or Gravity.START)
    }

    fun show() {
        agent.show()
    }
}
