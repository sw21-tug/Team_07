package com.example.pangea

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.facebook.FacebookSdk
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

    var resources: Resources = InstrumentationRegistry.getInstrumentation().targetContext.resources

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
        onView(withId(R.id.twitter_login_btn)).check(matches(withText(resources.getString(R.string.twitter_link_text))))
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
        onView(withId(R.id.twitter_login_btn)).check(matches(withText(resources.getString(R.string.twitter_unlink_text))))
    }

    @Test
    fun testFacebookLinkVisibleInAccountsFragment() {
        FacebookSdk.setApplicationId("171191854853298")
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        FacebookSdk.sdkInitialize(context)
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)
        val fbHandler = FacebookHandler(context, user)
        fbHandler.logoutFacebook()
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.ViewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.login_button_facebook)).check(matches(isDisplayed()))
        onView(withId(R.id.login_button_facebook)).check(matches(withText(resources.getString(R.string.facebook_link_text))))
    }

    @Test
    fun testFacebookHasLinkedAccount() {
        FacebookSdk.setApplicationId("171191854853298")
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        FacebookSdk.sdkInitialize(context)
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        val facebookOauthToken = "testtoken"
        dbHandler.saveFacebookLink(user, facebookOauthToken, context)

        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.ViewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.login_button_facebook)).check(matches(isDisplayed()))
        onView(withId(R.id.login_button_facebook)).check(matches(withText(resources.getString(R.string.facebook_unlink_text))))
    }

}