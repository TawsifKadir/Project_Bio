package com.xplo.code.suites

import com.xplo.code.ui_legacy.login.LoginActivityTest
import com.xplo.code.ui_legacy.login_otp.OtpLoginActivityTest
import com.xplo.code.ui_legacy.signup.SignupActivityTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/27/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginActivityTest::class,
    OtpLoginActivityTest::class,
    SignupActivityTest::class
)
class LoginTestSuite