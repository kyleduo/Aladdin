package com.kyleduo.aladdin.api.manager.view

import androidx.annotation.IntDef

/**
 * @author kyleduo on 2021/6/15
 */
object SnapEdge {

    const val NONE = 0
    const val LEFT = 1
    const val TOP = 1.shl(1)
    const val RIGHT = 1.shl(2)
    const val BOTTOM = 1.shl(3)
    const val ALL = LEFT or TOP or RIGHT or BOTTOM

}

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(
    SnapEdge.NONE,
    SnapEdge.LEFT,
    SnapEdge.TOP,
    SnapEdge.RIGHT,
    SnapEdge.BOTTOM,
    SnapEdge.ALL
)
annotation class SnapEdgeType
