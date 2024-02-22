package com.xplo.data.network.interceptor

import android.os.SystemClock
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class RetryInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var tryCount = 0
        while (!response.isSuccessful && response.code == 425 && tryCount < 3) {
            //tryCount++
            try {
                SystemClock.sleep(3000)
                response = chain.proceed(request)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                tryCount++
            }
        }
        return response
    }
}