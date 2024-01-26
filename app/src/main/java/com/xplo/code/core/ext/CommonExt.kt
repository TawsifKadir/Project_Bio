package com.xplo.code.core.ext

import android.widget.Spinner

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 02/09/22
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

fun Double?.isNull(): Boolean {
    return this != null
}

fun Spinner.setItem(items: Array<String>, item: String?) {
    if (item == null) return
    val index = items.indexOf(item)
    this.post { this.setSelection(index) }
}
