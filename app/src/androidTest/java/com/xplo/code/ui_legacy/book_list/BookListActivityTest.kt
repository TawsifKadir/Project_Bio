package com.xplo.code.ui_legacy.book_list

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.xplo.code.EspressoTestingIdlingResource
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
 * Created  : 5/24/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class BookListActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(BookListActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource())
    }

    @Test
    fun initView() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    fun checkRecyclerView() {

        // verify recycler view is displayed
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        // perform click on item at 0th position
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        val scenario = activityRule.scenario

        // verify the toast text
//        val activity = activityRule.activity
//        onView(withText("Title : 'IT' Rating : '7.6'"))
//            .inRoot(withDecorView(not(`is`(activity.window.decorView))))
//            .check(matches(isDisplayed()))

    }
}