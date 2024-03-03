package com.xplo.code

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers.not

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/27/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
open class EHelper {

    fun checkVisible(viewId: Int) {
        onView(withId(viewId)).check(matches(isDisplayed()))
    }

    fun checkNotVisible(viewId: Int) {
        onView(withId(viewId)).check(matches(not(isDisplayed())))
    }

    fun clickOn(viewId: Int) {
        onView(withId(viewId)).perform(click())
    }


}