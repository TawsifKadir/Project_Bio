package com.xplo.code.ui.splash

import android.util.Log
import com.xplo.code.base.BasePresenter

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/16/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class SplashPresenter : BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    companion object {
        private const val TAG = "SplashPresenter"
    }

    override fun splashDelay(delayTime: Int) {

        val timer = object : Thread() {
            override fun run() {
                try {
                    sleep((delayTime * 1000).toLong())
                } catch (e: Exception) {
                    Log.e(TAG, "splashDelay: Sleep Didn't Work properly", e)
                } finally {
                    view?.navigateToNext()
                }
            }
        }

        timer.start()
    }

    override fun tryAnonymousLogin(deviceId: String) {

//        NetworkCallImpl().generateToken(LoginRqBody(deviceId), object : NRCallback<TokenResponse> {
//            override fun onSuccess(data: TokenResponse?, callInfo: CallInfo?) {
//                //view?.onAuthCompleted(data.token)
//            }
//
//            override fun onFailure(th: Throwable, callInfo: CallInfo?) {
//
//            }
//
//        })
    }

}