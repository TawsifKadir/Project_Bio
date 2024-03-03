package com.xplo.code.ui_legacy.main_act

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.xplo.code.R
import com.xplo.code.ui.main_act.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright 2020 (C) xplo
 *
 *
 * Created  : 5/26/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initView() {

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.frame)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigation)).check(matches(isDisplayed()))
        onView(withId(R.id.fab)).check(matches(isDisplayed()))

        //onView(withId(R.id.toolbar)).check(matches(withText("Home")))

    }



}