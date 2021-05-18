package com.kyleduo.aladdin.utils

import android.content.Context
import com.kyleduo.aladdin.Aladdin

/**
 * @author kyleduo on 2021/5/18
 */

/**
 * retrieve context instance conveniently through Aladdin
 */
internal val Aladdin.app: Context
    get() = context.application