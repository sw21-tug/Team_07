package com.example.pangea

import androidx.annotation.UiThread
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class DashboardInstrumentedTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(DashboardsActivity::class.java)

    @Test
    fun testBar() {
        onView(withId(R.id.dashboard_bar)).check(matches(isDisplayed()))
    }

    @Test
    @UiThread
    fun checkTabSwitch() {
        onView(Matchers.allOf(ViewMatchers.withText("Accounts"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))
        onView(Matchers.allOf(ViewMatchers.withText("Posts"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))
        onView(Matchers.allOf(ViewMatchers.withText("Bookmarked"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))
    }

    @Test
    fun swipePage() {
        onView(withId(R.id.dashboard_bar))
                .check(matches(isDisplayed()))
        onView(withId(R.id.dashboard_bar))
                .perform(ViewActions.swipeRight())
    }
}