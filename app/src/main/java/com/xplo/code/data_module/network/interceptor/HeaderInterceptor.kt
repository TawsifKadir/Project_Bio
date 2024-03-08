package com.xplo.code.data_module.network.interceptor

import android.util.Log
import com.xplo.code.core.ext.toBool
import com.xplo.code.data.pref.PrefHelperImpl
import com.xplo.code.data_module.core.Config
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class HeaderInterceptor @Inject constructor() : Interceptor {
    private val TAG = "HeaderInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "intercept() called with: chain = $chain")
        val builder = chain.request().newBuilder()

        builder.addHeader("Content-Type", "application/json")
        if (Config.ACCESS_TOKEN?.isNotBlank().toBool()) {
            builder.addHeader("Authorization", "Bearer ${Config.ACCESS_TOKEN}")
        }

//        val token = PrefHelperImpl().getAccessToken()
//        if (token?.isNotBlank().toBool()){
//            builder.addHeader("Authorization", "Bearer ${token}")
//        }


        Config.DEVICE_ID?.let { builder.addHeader("DeviceId", it) }
        //builder.addHeader("Accept-Language", Config.LOCALE)

        return chain.proceed(builder.build())
    }
}