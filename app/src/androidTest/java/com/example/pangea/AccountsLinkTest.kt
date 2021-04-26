package com.example.pangea

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test

class AccountsLinkTest {



    @get:Rule
    val mActivityTestRule: ActivityTestRule<DashboardsActivity> =
        object : ActivityTestRule<DashboardsActivity>(DashboardsActivity::class.java, true, false) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                return Intent(targetContext, DashboardsActivity::class.java).apply {
                    putExtra("loggedInUserMail", "test.user@test.com")
                }
            }
        }

    @Test
    fun testTwitterLinkVisibleInAccountsFragment() {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        var tHandler = TwitterHandler(context, user)
        tHandler.unlinkAccount()

        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.ViewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.twitter_login_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.twitter_login_btn)).check(matches(withText("Login with Twitter")))
    }

    @Test
    fun testTwitterHasLinkedAccount() {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        val twitterAuthToken = "token"
        val twitterAuthTokenSecret = "secret"
        dbHandler.saveTwitterLink(user, twitterAuthToken, twitterAuthTokenSecret, context)

        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.ViewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.twitter_login_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.twitter_login_btn)).check(matches(withText("Unlink twitter account")))
    }




}