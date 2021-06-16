package com.kyleduo.aladdin.genie.hook

import java.lang.ref.Reference

/**
 * @author kyleduo on 2021/6/17
 */
interface OnReferenceRecycledListener {

    fun onReferenceRecycled(reference: Reference<*>)
}