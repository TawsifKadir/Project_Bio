package com.xplo.data.core

import android.annotation.SuppressLint
import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho

/**
 * Copyright 2019 (C) Xplo
 *
 * Created  : 5/28/2019
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@SuppressLint("StaticFieldLeak")
object InterceptorHelper {

    //var context: Context? = null

    val isStethoEnabled = true
    val isFlipperEnabled = true
    //val isOkHttpProfilerEnabled = true
    val isChuckerEnabled = true


    var flipperPlugin: NetworkFlipperPlugin? = null

    fun init(context: Context) {
        initStetho(context)
        initFlipper(context)
        // okhttp, curl doesn't need init
    }

    fun initStetho(context: Context) {
        if (!isStethoEnabled) return
        Stetho.initializeWithDefaults(context)
    }

    fun initFlipper(context: Context) {
        if (!isFlipperEnabled) return
        val flipperPlugin = NetworkFlipperPlugin()
        this.flipperPlugin = flipperPlugin
        SoLoader.init(context, SoLoader.SOLOADER_ALLOW_ASYNC_INIT)
        AndroidFlipperClient.getInstance(context).apply {
            addPlugin(flipperPlugin)
            start()
        }
    }

}