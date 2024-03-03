package com.xplo.code.data_module.legacy_code.call

import android.util.Log
import com.xplo.code.data_module.core.CallInfo
import com.xplo.code.data_module.core.Config
import com.xplo.code.data_module.core.NRCallback
import com.xplo.code.data_module.legacy_code.api.ApiClient
import com.xplo.code.data_module.legacy_code.api.ApiEndpoint
import com.xplo.code.data_module.model.user.LoginRqb
import com.xplo.code.data_module.model.user.TokenRsp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-11-10
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class NetworkCallImpl : NetworkCall {


    companion object {
        private const val TAG = "NetworkCall"
    }

    override fun generateToken(body: LoginRqb, callback: com.xplo.code.data_module.core.NRCallback<TokenRsp>?) {
        Log.d(TAG, "generateToken() called with: body = $body, callback = $callback")
        val apiEndpoint = ApiClient.getClient(null).create(ApiEndpoint::class.java)

        val call = getApiEndpoint().generateToken(body)

        call.enqueue(object : Callback<TokenRsp> {
            override fun onResponse(call: Call<TokenRsp>, response: Response<TokenRsp>) {
                Log.d(
                    TAG,
                    "generateToken: onResponse() called with: call = $call, response = ${response.body()}"
                )

                val callInfo =
                    com.xplo.code.data_module.core.CallInfo(response.code(), response.message())
                if (response.isSuccessful) {
                    callback?.onSuccess(response.body(), callInfo)
                } else {
                    callback?.onFailure(Throwable(response.message()), callInfo)
                }
            }

            override fun onFailure(call: Call<TokenRsp>, t: Throwable) {
                Log.d(TAG, "generateToken: onFailure() called with: call = $call, t = $t")
                callback?.onFailure(t, null)
            }

        })


    }

    private fun getLocale(): String {
        return "en"
        //return prefHelper.getLocale()
    }

    private fun getPlatform(): String {
        return "BaseConstants.PLATFORM_NAME"
    }

    private fun getApiEndpoint(): ApiEndpoint {
        val accessToken = com.xplo.code.data_module.core.Config.ACCESS_TOKEN
        return ApiClient.getClient(accessToken).create(ApiEndpoint::class.java)
    }


}

