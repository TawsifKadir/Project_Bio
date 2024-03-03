package com.xplo.code.data_module.network.interceptor

import android.util.Log
import com.xplo.code.data_module.core.Config
import com.xplo.code.data_module.core.ext.toBool
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class HeaderInterceptor @Inject constructor() : Interceptor {
    private val TAG = "HeaderInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "intercept() called with: chain = $chain")
        val builder = chain.request().newBuilder()

        builder.addHeader("Content-Type", "application/json")
        if (com.xplo.code.data_module.core.Config.ACCESS_TOKEN?.isNotBlank().toBool()){
            builder.addHeader("Authorization", "Bearer ${com.xplo.code.data_module.core.Config.ACCESS_TOKEN}")
        }
        builder.addHeader("DeviceId", "d5a58ff3-dc14-4333-8076-72b0fb4cab7a")
        //builder.addHeader("Accept-Language", Config.LOCALE)

        return chain.proceed(builder.build())
    }
}