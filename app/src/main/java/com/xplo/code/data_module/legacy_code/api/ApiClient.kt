package com.xplo.code.data_module.legacy_code.api

import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder

import com.xplo.code.BuildConfig
import com.xplo.code.data_module.core.Config
import com.xplo.code.data_module.network.interceptor.CurlInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-11-10
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 * if you get this error -> MalformedJsonException: Use JsonReader.setLenient(true)
 * https://stackoverflow.com/questions/39918814/use-jsonreader-setlenienttrue-to-accept-malformed-json-at-line-1-column-1-path
 * and remove unwanted echo in php
 */

object ApiClient {

    //private var retrofit: Retrofit? = null
    //private val baseUrl = BuildConfig.BASE_URL
    private val gson = GsonBuilder().setLenient().create()

    /**
     * If you need live environment at debug mode enable it
     */
    private const val isForceLiveEnvironment: Boolean = false

    /**
     * Retrofit client with/without auth token
     */
    private var client: Retrofit? = null

    /**
     * Method to get base url based on configuration
     */
    @JvmStatic
    val baseUrl: String
        get() {
            return com.xplo.code.data_module.core.Config.getBaseUrl()
        }

    /**
     * Create a retrofit client with any host and token
     */
    fun getRetrofit(host: String, token: String?): Retrofit {

        return Retrofit.Builder()
            .baseUrl(host)
            .client(getOkHttpClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Create a retrofit client with base url and auth token
     */
    fun getClient(token: String?): Retrofit {
        if (client != null) return client!!

        client = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return client!!
    }

    /**
     * Create a OkHttpClient with or without token
     * Interceptor enabled for debug mode
     * @param token can be null
     */
    private fun getOkHttpClient(token: String?): OkHttpClient {

        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(1, TimeUnit.MINUTES)
        httpClient.readTimeout(25, TimeUnit.SECONDS)
        httpClient.connectTimeout(25, TimeUnit.SECONDS)
        httpClient.addInterceptor(HeaderInterceptor(token, getAccessCode()))

        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(getLoggingInterceptor())
            httpClient.addNetworkInterceptor(StethoInterceptor())
            httpClient.addInterceptor(getCurlInterceptor())
        }

        return httpClient.build()
    }


    /**
     * Create a logging interceptor
     */
    private fun getLoggingInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private fun getCurlInterceptor(): Interceptor {
        return CurlInterceptor { message ->
            Log.d("CURL", message)
        }
    }

    fun invalidateAuthClient() {
        client = null
    }

    private fun getAccessCode(): String? {
        return null
        //return Utils.getAccessCode(RMemory.countryCode)
    }

    /**
     * A header interceptor class
     */
    class HeaderInterceptor(val token: String?, private val accessCode: String?) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            builder.addHeader("Content-Type", "application/json")
            if (token != null) builder.addHeader("Authorization", "Bearer $token")
            if (accessCode != null) builder.addHeader("Access-Code", accessCode)
            return chain.proceed(builder.build())
        }


    }

    /**
     * Create individual api service with token
     */
    fun <S> createApiService(serviceClass: Class<S>, token: String): S {
        return getClient(token).create(serviceClass)
    }

    /**
     * Create individual api service without token
     */
    fun <S> createApiService(serviceClass: Class<S>): S {
        return getClient(null).create(serviceClass)
    }

}