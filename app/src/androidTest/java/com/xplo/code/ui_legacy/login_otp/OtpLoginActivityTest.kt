package com.xplo.code.ui_legacy.login_otp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.xplo.code.EHelper
import com.xplo.code.R
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
class OtpLoginActivityTest : EHelper() {

    @get:Rule
    val activityRule = ActivityScenarioRule(OtpLoginActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initView() {

        checkVisible(R.id.llInputNum)
        checkVisible(R.id.ccp)
        checkVisible(R.id.etPhone)
        checkVisible(R.id.btOtpSend)

        checkNotVisible(R.id.llInputOtp)
        checkNotVisible(R.id.tvTitleOtp)
        checkNotVisible(R.id.etOtp)
        checkNotVisible(R.id.btOtpVerify)
        checkNotVisible(R.id.btOtpResend)
        checkNotVisible(R.id.btWrongNumber)

    }

}