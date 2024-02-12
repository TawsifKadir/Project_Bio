package com.xplo.code.core.ext

import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xplo.code.R

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

fun Int?.plusOne(): Int {
    if (this == null) return 0
    return this.plus(1)
}

fun Boolean?.toBool(): Boolean {
    if (this == null) return false
    return this
}

fun String?.isYes(): Boolean {
    if (this == null) return false
    return this.equals("Yes", ignoreCase = true)
}

fun String?.isNo(): Boolean {
    if (this == null) return false
    return this.equals("No", ignoreCase = true)
}

fun Spinner.setItem(items: Array<String>, item: String?) {
    if (item == null) return
    val index = items.indexOf(item)
    this.post { this.setSelection(index) }
}

fun RadioGroup?.checkRbPosNeg(rbPos: RadioButton, rbNeg: RadioButton, item: String?) {
    if (this == null) return
    if (item.isNullOrEmpty()) return

    if (item.equals("yes", ignoreCase = true)) {
        rbPos.isChecked = true
    } else {
        rbNeg.isChecked = true
    }

}

fun RadioGroup?.checkRbOpABforIDcard(rbA: RadioButton, rbB: RadioButton, item: String?) {
    if (this == null) return
    if (item.isNullOrEmpty()) return

    if (item.equals("Yes", ignoreCase = true)) {
        rbA.isChecked = true
    } else {
        rbB.isChecked = true
    }

}
fun RadioGroup?.checkRbOpAB(rbA: RadioButton, rbB: RadioButton, item: String?) {
    if (this == null) return
    if (item.isNullOrEmpty()) return

    if (item.equals("public works", ignoreCase = true)) {
        rbA.isChecked = true
    } else {
        rbB.isChecked = true
    }

}

fun TextView?.setValue(value: Int?) {
    if (this == null) return
    this.text = value.toString()
}


fun ImageView?.loadImage(url: String?, phId: Int = R.drawable.ph_content_portrait) {
    if (this == null) return
    if (url == null) return
    Glide.with(this.context)
        .load(url)
        .placeholder(phId)
        .into(this)
}

fun ImageView?.loadAvatar(url: String?) {
    if (this == null) return
    if (url == null) return
    this.loadImage(url, R.drawable.ic_avatar_3)
}