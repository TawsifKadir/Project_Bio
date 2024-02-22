package com.xplo.code.ui_legacy.login

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.xplo.code.EHelper
import com.xplo.code.R
import com.xplo.code.ui.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright 2020 (C) xplo
 *
 *
 * Created  : 5/27/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest : EHelper() {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initView() {

        checkVisible(R.id.ivBannar)
        checkVisible(R.id.etUserId)
        checkVisible(R.id.etPassword)
        checkVisible(R.id.cbShowPassword)
        checkVisible(R.id.btLogin)
        checkVisible(R.id.btLoginOtp)
        checkVisible(R.id.btSignup)

    }

    @Test
    fun openOtpLogin() {

        clickOn(R.id.btLoginOtp)

        // otp login view
        checkVisible(R.id.llInputNum)
    }

    @Test
    fun openSignup() {

        clickOn(R.id.btSignup)

        // signup view
        checkVisible(R.id.tvUserId)

    }

    @Test
    fun backToLoginFromSignup() {

        clickOn(R.id.btSignup)

        // signup view
        checkVisible(R.id.tvUserId)
        pressBack()

        // login view
        checkVisible(R.id.btLoginOtp)

    }
}