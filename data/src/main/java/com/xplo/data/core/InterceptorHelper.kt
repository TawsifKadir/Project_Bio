package com.xplo.data.core

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
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

object InterceptorHelper {

    val isStethoEnabled = true
    val isFlipperEnabled = true

    //val isOkHttpProfilerEnabled = true
    val isChuckerEnabled = true


    var flipperPlugin: NetworkFlipperPlugin? = null

    var chuckerCollector: ChuckerCollector? = null
    var chuckerInterceptor: ChuckerInterceptor? = null

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

    fun initChucker(context: Context) {
        if (!isChuckerEnabled) return
        chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        chuckerInterceptor = createChuckerInterceptor(context, chuckerCollector)
    }

    fun createChuckerInterceptor(
        context: Context?,
        collector: ChuckerCollector?
    ): ChuckerInterceptor? {
        if (context == null) return null
        if (collector == null) return null
        return ChuckerInterceptor.Builder(context)
            .collector(collector)
            // List of headers to replace with ** in the Chucker UI
            .redactHeaders("Authorization", "Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .createShortcut(true)
            .build()
    }

}