package com.kyleduo.aladdin.genies.board

import com.kyleduo.aladdin.api.manager.genie.AladdinGenie

/**
 * @author kyleduo on 2021/5/31
 */
class BoardGenie : AladdinGenie() {
    private val board by lazy { AladdinBoard(context) }
    override val apiClass: Class<*> = BoardGenie::class.java

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
