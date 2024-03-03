package com.xplo.code.core.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object ScreenUtils {
    private const val TAG = "ScreenUtils"

    fun getDeviceHeight(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        val windowmanager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowmanager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getDeviceWidth(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        val windowmanager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowmanager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun screenDensity(context: Context): Float {
        val displayMetrics = DisplayMetrics()
        val windowmanager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowmanager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.density
    }


}