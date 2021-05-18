package com.example.pangea

import android.content.Context
import android.content.Intent
import androidx.annotation.UiThread
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardInstrumentedTests {
    @get:Rule
    val activityRule: ActivityTestRule<DashboardsActivity> =
            object : ActivityTestRule<DashboardsActivity>(DashboardsActivity::class.java, true, false) {
                override fun getActivityIntent(): Intent {
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    val sharedPref = targetContext.getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("current_user","test.user@test.com").apply()
                    return Intent(targetContext, DashboardsActivity::class.java)
                }
            }

    @Before
    fun setupUser() {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        DatabaseHandler().registerUser(email, pw, context)
    }


    @Test
    fun testBar() {
        activityRule.launchActivity(null)
        onView(withId(R.id.dashboard_bar)).check(matches(isDisplayed()))
    }

    @Test
    @UiThread
    fun checkTabSwitch() {
        activityRule.launchActivity(null)
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
        activityRule.launchActivity(null)
        onView(withId(R.id.dashboard_bar))
                .check(matches(isDisplayed()))
        onView(withId(R.id.dashboard_bar))
                .perform(ViewActions.swipeRight())
    }
}