package com.xplo.code.core.ext

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.Point
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 02/09/22
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */


fun View.getString(@StringRes resId: Int): String = resources.getString(resId)

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.getDrawableCompat(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)

fun Context.getResIdFromDrawable(imageName: String?): Int {
    return this.resources.getIdentifier(imageName, "drawable", this.packageName)
}

fun Context.getDimenSize(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

//fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}

fun Context.pToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text.orEmpty(), duration).show()
}

fun Context.shareTxt(text: String, subject: String = ""): Boolean {
    val intent = Intent()
    intent.type = "text/plain"
    intent.putExtra(EXTRA_SUBJECT, subject)
    intent.putExtra(EXTRA_TEXT, text)
    try {
        startActivity(createChooser(intent, null))
        return true
    } catch (e: ActivityNotFoundException) {
        return false
    }
}


fun Context.makeCall(number: String): Boolean {
    try {
        val intent = Intent(ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        return true
    } catch (e: Exception) {
        return false
    }
}

val Activity.screenWidth: Int
    get() = Point().apply {
        windowManager.defaultDisplay.getSize(this)
    }.x

val Activity.screenHeight: Int
    get() = Point().apply {
        windowManager.defaultDisplay.getSize(this)
    }.y

//fun Context.dpToPixels(dps: Float): Int {
//    return (dps * resources.displayMetrics.density + 0.5f).toInt()
//}

fun Context.isActivityFinishing(): Boolean = this is Activity && isFinishing

fun Context.isActivityDestroyed(): Boolean = this is Activity && isDestroyed
