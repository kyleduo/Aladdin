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
    companion object {
        private const val TAG_CONTENT = "content"
    }

    override val view: View by lazy {
        FrameLayout(Aladdin.app).apply {
            setBackgroundColor(0x60000000)
            if (findViewWithTag<View>(TAG_CONTENT) == null) {
                addView(contentView)
            }
            setOnClickListener {
                agent.dismiss()
                (Aladdin.genie("entry") as? EntryGenie)?.show()
            }

            setUpForPortrait()
        }
    }
    override val tag: Any = "Board"
    private val contentView: LinearLayout by lazy {
        LinearLayout(Aladdin.app).apply {
            tag = TAG_CONTENT
            val width = UIUtils.screenSize.width - 32.dp2px()
            layoutParams = FrameLayout.LayoutParams(width, width / 3 * 4).apply {
                gravity = Gravity.CENTER
            }
            setBackgroundColor(0xFFFAFAFA.toInt())
        }
    }

    private fun setUpForPortrait() {
        contentView.orientation = LinearLayout.VERTICAL
    }

    private fun setUpForLandscape() {
        contentView.orientation = LinearLayout.HORIZONTAL
    }

    fun show() {
        agent.show()
        agent.resize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
