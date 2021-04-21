package com.example.pangea

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountsLinkTest {
    @get:Rule
    val activityRule = ActivityTestRule(DashboardsActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun testTwitterLinkVisibleInAccountsFragment() {

        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = com.example.pangea.DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        val intent = Intent()
        intent.putExtra("loggedInUserMail", "test.user@test.com" )
        activityRule.launchActivity(intent)

        onView(withId(R.id.ViewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.twitter_login_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.twitter_login_btn)).check(matches(withText("Login with Twitter")))

    }

    @After
    fun cleanUp() {
        Intents.release()
    }

}