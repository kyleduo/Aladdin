package com.kyleduo.aladdin.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * get layout inflater from a context instance
 */
fun Context.layoutInflater(): LayoutInflater = LayoutInflater.from(this)

/**
 * inflate a view for this ViewGroup
 */
fun ViewGroup.inflateView(layoutRes: Int): View =
    context.layoutInflater().inflate(layoutRes, this, false)