package com.xplo.code.ui.splash

import com.xplo.code.base.BaseContract

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/16/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface SplashContract {

    interface View : BaseContract.View {

        fun navigateToNext()
        fun playSound()
        fun onAuthCompleted(token: String)


    }

    interface Presenter : BaseContract.Presenter<View> {
        /**
         * Delay in splash
         * @delayTime in second
         */
        fun splashDelay(delayTime: Int)

        fun tryAnonymousLogin(deviceId: String)
    }
}