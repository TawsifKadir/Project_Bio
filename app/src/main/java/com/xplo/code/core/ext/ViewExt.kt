package com.xplo.code.core.ext

import android.animation.AnimatorInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.AnimatorRes
import androidx.core.view.isVisible
import com.bumptech.glide.Glide

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 02/09/22
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.toggleVisibility() {
    if (this.isVisible) this.visibility = View.GONE
    else this.visibility = View.VISIBLE
}


fun View.doAnimator(@AnimatorRes animRes: Int) {
    AnimatorInflater.loadAnimator(context, animRes)?.apply {
        setTarget(this@doAnimator)
        start()
    }
}

val EditText.value
    get() = text?.toString() ?: ""

fun ImageView.loadFromUrl(imageUrl: String) {
    Glide.with(this).load(imageUrl).into(this)
}