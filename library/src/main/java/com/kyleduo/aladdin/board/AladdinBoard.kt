package com.kyleduo.aladdin.board

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.entry.EntryGenie
import com.kyleduo.aladdin.utils.UIUtils
import com.kyleduo.aladdin.utils.app
import com.kyleduo.aladdin.utils.dp2px
import com.kyleduo.aladdin.view.IAladdinView

/**
 * @author kyleduo on 2021/5/31
 */
class AladdinBoard : IAladdinView() {
    override val view: View by lazy {
        FrameLayout(Aladdin.app).apply {
            setUpForPortrait()
            setOnClickListener {
                agent.dismiss()
                (Aladdin.genie("entry") as? EntryGenie)?.show()
            }
        }
    }
    override val tag: Any = "Board"
    private val contentView: View by lazy {
        LinearLayout(Aladdin.app).apply {
            val width = UIUtils.screenSize.width - 32.dp2px()
            layoutParams = FrameLayout.LayoutParams(width, width / 3 * 4).apply {
                gravity = Gravity.CENTER
            }
            setBackgroundColor(0xFFFAFAFA.toInt())
        }
    }

    private fun FrameLayout.setUpForPortrait() {
        setBackgroundColor(0x60000000)
        addView(contentView)
    }

    fun show() {
        agent.show()
        agent.resize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
