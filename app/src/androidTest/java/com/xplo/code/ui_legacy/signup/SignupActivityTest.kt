package com.xplo.code.ui_legacy.signup

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
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
class SignupActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignupActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initView() {

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.ivBannar)).check(matches(isDisplayed()))
        onView(withId(R.id.tvUserId)).check(matches(isDisplayed()))
        onView(withId(R.id.etUserId)).check(matches(isDisplayed()))
        onView(withId(R.id.tvPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.tvPasswordAgain)).check(matches(isDisplayed()))
        onView(withId(R.id.etPasswordAgain)).check(matches(isDisplayed()))
        onView(withId(R.id.btSignup)).check(matches(isDisplayed()))


    }
}