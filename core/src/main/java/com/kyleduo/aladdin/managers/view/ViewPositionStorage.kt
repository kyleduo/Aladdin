package com.kyleduo.aladdin.managers.view

import android.content.Context
import android.graphics.Point
import com.kyleduo.aladdin.api.AladdinContext
import com.kyleduo.aladdin.api.manager.view.AladdinView

/**
 * Persisting view's position
 *
 * @author kyleduo on 2021/6/17
 */
class ViewPositionStorage(
    private val context: AladdinContext
) {

    companion object {
        private const val SP_NAME = "aladdin_view_position"
    }

    private val sp by lazy {
        context.app.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    fun save(view: AladdinView) {
        val value = "${view.agent.getX()},${view.agent.getY()}"
        sp.edit().putString(view.tag, value).apply()
    }

    fun get(view: AladdinView): Point? {
        val value = sp.getString(view.tag, null) ?: return null
        val segments = value.split(",")
        if (segments.size != 2) {
            return null
        }
        return try {
            val x = segments[0].toInt()
            val y = segments[1].toInt()
            Point(x, y)
        } catch (e: NumberFormatException) {
            null
        }
    }
}