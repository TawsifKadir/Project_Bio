package com.xplo.code.ui.login

import androidx.fragment.app.Fragment
import com.xplo.code.base.BaseContract
import com.xplo.code.data_module.model.user.ResetPassRsp
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

        fun navigateToSignUp()
        fun navigateToReset(userId: String?)
        fun doFragmentTransaction(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean,
            clearBackStack: Boolean
        )

        fun onLoginSuccess(token: String, id: String?)
        fun onLoginFailure(msg: String?)


    }

    interface LoginView : BaseContract.View {

        fun navigateToSignup()
        fun navigateToResetPassword(userId: String?)

        fun onClickLogin()
        fun onNormalLogin(credentials: LoginCredentials)
        fun onOfflineLogin(credentials: LoginCredentials)

        fun onLoginWithSdk(credentials: LoginCredentials)


        fun onLoginSuccess(token: String, username: String?)
        fun onLoginPending(token: String, username: String?)
        fun onLoginFailure(msg: String?)

    }

    interface SignUpView : BaseContract.View {

        fun backToLogin()
        fun onSignUpSuccess(id: String?)
        fun onSignUpFailure(msg: String?)

    }

    interface ResetPasswordView : BaseContract.View {
        fun backToLogin()

        fun onClickResetPassword()
        fun onResetPasswordSuccess(rsp: ResetPassRsp?)
        fun onResetPasswordFailure(msg: String?)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun anonymousLogin()
        fun passwordLogin(loginCredentials: LoginCredentials)

    }


}