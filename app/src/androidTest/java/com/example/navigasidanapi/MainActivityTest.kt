package com.example.navigasidanapi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{
    private val sleepTime: Long = 2000
    @get:Rule var activityRule= activityScenarioRule<MainActivity >()


    @Test
    fun testFragmentView() {

        onView(withId(R.id.fragHome)).check(matches(isDisplayed()))

        onView(withId(R.id.btnLove)).perform(click())

        onView(withId(R.id.favFragment)).check(matches(isDisplayed()))

        Thread.sleep(sleepTime)

    }
}