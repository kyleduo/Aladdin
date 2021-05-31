package com.kyleduo.aladdin.board

import com.kyleduo.aladdin.Aladdin
import com.kyleduo.aladdin.genies.IGenie

/**
 * @author kyleduo on 2021/5/31
 */
class BoardGenie : IGenie {
    override val key: String = "board"
    private val board = AladdinBoard()

    override fun onStart() {
        Aladdin.context.viewManager.register(board)
    }

    override fun onStop() {
    }

    fun show() {
        board.show()
    }
}
