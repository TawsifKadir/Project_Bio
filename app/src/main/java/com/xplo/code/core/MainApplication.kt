package com.xplo.code.core

import android.app.Application
import com.xplo.code.data.RMemory
import com.xplo.code.data.pref.PrefHelperImpl
import com.xplo.code.utils.Utils
import dagger.hilt.android.HiltAndroidApp

/**
 * Copyright 2019 (C) Xplo
 *
 * Created  : 5/28/2019
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Contextor.getInstance().init(applicationContext)
        //com.xlib.limeutils.core.Contextor.getInstance().init(applicationContext)

        //FirebaseApp.initializeApp(this)


        initDataModule()

        initInterceptor()

    }


    private fun initDataModule() {
       // com.xplo.data.core.Contextor.context = this

        //setting config for data module
        //Config.PLATFORM_ID = "abfea462-f64d-491e-9cd9-75ee001f45b0"
        //Config.ACCESS_CODE = Utils.getAccessCode(RMemory.countryCode)
        //Config.ACCESS_TOKEN = PrefHelperImpl().getAccessToken()
        //Config.LOCALE = "bn"
        //Config.BASE_URL = BuildConfig.BASE_URL
    }


    private fun initInterceptor() {

//        Stetho.initializeWithDefaults(this)
//
//        // flipper
//        val flipperPlugin = NetworkFlipperPlugin()
//        InterceptorUtils.flipperPlugin = flipperPlugin
//        SoLoader.init(this, SoLoader.SOLOADER_ALLOW_ASYNC_INIT)
//        AndroidFlipperClient.getInstance(this).apply {
//            addPlugin(flipperPlugin)
//            start()
//        }

        //InterceptorHelper.init(this)


    }

}