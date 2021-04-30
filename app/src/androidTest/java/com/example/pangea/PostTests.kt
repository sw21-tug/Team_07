package com.example.pangea

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pangea.Posts
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class PostTests {
        @get:Rule var rule = ActivityScenarioRule(DashboardsActivity::class.java)

        @Test
        fun testButton () {
            rule.scenario
            onView(withId(R.id.sendpostbtn)).perform(click())

            onView(withId(R.id.postbtn)).check(matches(isDisplayed()))
            onView(withId(R.id.textfield)).check(matches(isDisplayed()))
            onView(withId(R.id.fbcheck)).check(matches(isDisplayed()))
            onView(withId(R.id.twittercheck)).check(matches(isDisplayed()))

            onView(withId(R.id.fbcheck)).perform(click())
            onView(withId(R.id.fbcheck)).check(matches(isChecked()))

            onView(withId(R.id.twittercheck)).perform(click())
            onView(withId(R.id.twittercheck)).check(matches(isChecked()))
        }

}