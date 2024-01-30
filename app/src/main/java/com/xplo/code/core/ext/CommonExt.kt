package com.xplo.code.core.ext

import android.widget.Spinner
import android.widget.TextView

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

fun String?.isYes(): Boolean {
    if (this == null) return false
    return this.equals("Yes")
}

fun String?.isNo(): Boolean {
    if (this == null) return false
    return this.equals("No")
}

fun Spinner.setItem(items: Array<String>, item: String?) {
    if (item == null) return
    val index = items.indexOf(item)
    this.post { this.setSelection(index) }
}

fun TextView?.setValue(value: Int?) {
    if (this == null) return
    this.text = value.toString()
}
