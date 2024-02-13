package com.xplo.data.di

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
//import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
//import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.google.gson.Gson
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.xplo.data.core.Config
import com.xplo.data.core.Contextor
import com.xplo.data.network.api.ContentApi
import com.xplo.data.network.api.UserApi
import com.xplo.data.network.interceptor.CurlInterceptor
import com.xplo.data.network.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 02/01/22
 * Updated  :
 * Author   : xplo
 * Desc     : Its a module class, provide necessary dependency for data module
 * Comment  : Very sensitive code, Make changes if needed but,
 *            Please don't commit any changes without review with other dev
 */

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val TAG = "DataModule"

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor { msg ->
        Log.d("OKHTTP", msg)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideCurlInterceptor() = CurlInterceptor { msg ->
        Log.d("CURL", msg)
    }

    @Provides
    fun provideHeaderInterceptor() = HeaderInterceptor()

//    @Provides
//    fun provideRetryInterceptor() = RetryInterceptor()

    @Provides
    fun provideOkhttpProfilerInterceptor() = OkHttpProfilerInterceptor()

//    @Provides
//    fun provideNetworkFlipperPlugin() = NetworkFlipperPlugin()
//
//    @Provides
//    fun provideFlipperOkhttpInterceptor(plugin: NetworkFlipperPlugin) =
//        FlipperOkhttpInterceptor(plugin)
//
//
    @Provides
    fun provideChuckerCollector(context: Context): ChuckerCollector {
        return ChuckerCollector(context, showNotification = true)
    }

    @Provides
    fun provideChuckerInterceptor(
        context: Context,
        collector: ChuckerCollector
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(collector)
            // Controls Android shortcut creation.
            .createShortcut(true)
            .build()
    }

    @Provides
    fun provideContext() = Contextor.context


    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideOkhttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        curlInterceptor: CurlInterceptor,
        okHttpProfilerInterceptor: OkHttpProfilerInterceptor,
        headerInterceptor: HeaderInterceptor,
        //flipperInterceptor: FlipperOkhttpInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(curlInterceptor)
            //.addInterceptor(flipperInterceptor)
            .addInterceptor(chuckerInterceptor)
            .callTimeout(25, TimeUnit.SECONDS)
            .addInterceptor(okHttpProfilerInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideUserApi(client: OkHttpClient): UserApi =
        createRetrofitClient(client).create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideContentApi(client: OkHttpClient): ContentApi =
        createRetrofitClient(client).create(ContentApi::class.java)

    /**
     * Method to create retrofit client for saas
     */
    private fun createRetrofitClient(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Config.getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()


}
