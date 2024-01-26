package com.xplo.data.network.interceptor

import com.xplo.data.core.Config
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class HeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Authorization", "Bearer ${Config.ACCESS_TOKEN}")
        builder.addHeader("Accept-Language", Config.LOCALE)

        return chain.proceed(builder.build())
    }
}