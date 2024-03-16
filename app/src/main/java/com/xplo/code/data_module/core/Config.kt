package com.xplo.code.data_module.core

import android.util.Log

import com.xplo.code.BuildConfig


object Config {
    var TAG = "Config"

    /**
     * product code
     */
    var PRODUCT_CODE = "xdb"

//    /**
//     * enable disable debug environment
//     */
//    var IF_DEBUG = true

    private var BASE_URL = "https://snsopafis.southsudansafetynet.info/"
    //var BASE_URL_LEGACY = "https://api.xdb-solutions.com/"
    private var BASE_URL_STAGE = "http://snsopafis.karoothitbd.com:8090/"


    var LOCALE = "bn"
    var PLATFORM_NAME = "universal"
    var ACCESS_CODE: String? = null
    var ACCESS_TOKEN: String? = null
    var PLATFORM_ID: String? = null
    var DEVICE_ID: String? = null


    // no effect in release apk
    var API_TYPE = ApiType.LIVE


    fun getBaseUrl(): String {

        var url = BASE_URL

        if (!BuildConfig.DEBUG) return url

        url = when (API_TYPE) {
            ApiType.LIVE -> BASE_URL
            ApiType.STAGE -> BASE_URL_STAGE
        }

        return url
    }

    fun printDetails() {
        //Log.d(TAG, "token: $ACCESS_TOKEN")
        //Log.d(TAG, "BASE_URL: $BASE_URL")
    }
}