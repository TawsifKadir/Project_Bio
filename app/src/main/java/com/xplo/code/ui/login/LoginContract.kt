package com.xplo.code.ui.login

import com.xplo.code.base.BaseContract
import com.xplo.code.ui.login.model.LoginCredentials

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface LoginContract {

    enum class LoginMethod {
        ANONYMOUS, PASSWORD, OTP, AUTO, GOOGLE, FACEBOOK
    }


    interface View : BaseContract.View {

        fun navigateToNext()
        fun navigateToSignup()
        fun navigateToOtpLogin()

        fun onLoginSuccess(token: String, id: String?)
        fun onLoginFailure(msg: String?)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun anonymousLogin()
        fun passwordLogin(loginCredentials: LoginCredentials)

    }


}