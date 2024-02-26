package com.xplo.data.network.interceptor

import android.util.Log
import com.xplo.data.core.Config
import com.xplo.data.core.ext.toBool
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class HeaderInterceptor @Inject constructor() : Interceptor {
    private val TAG = "HeaderInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "intercept() called with: chain = $chain")
        val builder = chain.request().newBuilder()

        builder.addHeader("Content-Type", "application/json")
        if (Config.ACCESS_TOKEN?.isNotBlank().toBool()){
            builder.addHeader("Authorization", "Bearer ${Config.ACCESS_TOKEN}")
        }
        builder.addHeader("DeviceId", "47951385-a13f-409a-9a79-c4aaef0e3f9b")
        //builder.addHeader("Accept-Language", Config.LOCALE)

        return chain.proceed(builder.build())
    }
}