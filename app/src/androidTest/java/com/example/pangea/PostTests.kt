package com.example.pangea

import android.content.Context
import android.content.Intent
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
import androidx.test.rule.ActivityTestRule
import com.example.pangea.Posts
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class PostTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(PostsPopup::class.java)
    @Test
        fun testButton ()
        {
            val email = "test.user@test.com"
            val pw = "1234abc"
            val context = ApplicationProvider.getApplicationContext<Context>()
            val dbHandler = DatabaseHandler()
            dbHandler.registerUser(email, pw, context)
            var user = dbHandler.getRegisteredUser(email, context)
            Assert.assertEquals(email, user.email)
            Assert.assertEquals(pw, user.password)

            onView(withId(R.id.facebookCheck)).check(matches(isDisplayed()))
            onView(withId(R.id.twitterCheck)).check(matches(isDisplayed()))
            onView(withId(R.id.plain_text_input)).check(matches(isDisplayed()))

            onView(withId(R.id.facebookCheck)).perform(click())
            onView(withId(R.id.facebookCheck)).check(matches(isChecked()))

            onView(withId(R.id.twitterCheck)).perform(click())
            onView(withId(R.id.twitterCheck)).check(matches(isChecked()))
        }

}