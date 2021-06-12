package com.kyleduo.aladdin.genies.board

import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

/**
 * @author kyleduo on 2021/5/31
 */
class BoardGenie(context: AladdinContext) : AladdinGenie(context) {
    override val key: String = "board"
    private val board = AladdinBoard(context)

    override fun onStart() {
        context.viewManager.register(board)

        board.addGenies(context.genieManager.allGenies())
    }

    override fun onStop() {
    }

    fun show() {
        board.show()
    }
}
