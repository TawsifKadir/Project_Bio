package com.xplo.code.core.ext

import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.xplo.code.BuildConfig

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 02/09/22
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

fun dbgBlock(caller: () -> Unit) {
    if (BuildConfig.DEBUG)
        run(caller)
}


fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}


fun Window.adjustScreenBrightness(brightness: Float) {
    val validBrightness = brightness == -1f || brightness in 0f..1f
    if (validBrightness) {
        val params: WindowManager.LayoutParams = attributes
        // range from 0 to 1, specify -1 for default brightness
        params.screenBrightness = brightness
        attributes = params
    }
}

inline fun aboveApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
        block()
    }
}

inline fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
        block()
    }
}