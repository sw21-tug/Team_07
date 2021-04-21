package com.example.pangea

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class TwitterInstrumentedTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(AccountsActivity::class.java)

    @Test
    fun testTwitterLinkVisible() {

        onView(withId(R.id.twitter_login_btn)).check(matches(isDisplayed()))
    }
}