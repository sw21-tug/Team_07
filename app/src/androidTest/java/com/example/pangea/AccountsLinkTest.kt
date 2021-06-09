package com.example.pangea

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.facebook.FacebookSdk
import junit.framework.Assert
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.After
import org.junit.Before

class AccountsLinkTest {

    @get:Rule
    val activityRule: ActivityTestRule<DashboardsActivity> =
        object : ActivityTestRule<DashboardsActivity>(DashboardsActivity::class.java, true, false) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                val sharedPref = targetContext.getSharedPreferences("user", Context.MODE_PRIVATE)
                sharedPref.edit().putString("current_user","accountsTest.user@test.com").apply()
                return Intent(targetContext, DashboardsActivity::class.java)
            }
        }

    var resources: Resources = InstrumentationRegistry.getInstrumentation().targetContext.resources

    @Before
    fun setUp() {
        Intents.init()
        val email = "accountsTest.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
    }

    @After
    fun cleanUp() {
        Intents.release()
        AppDatabase.destroyInstance()
    }

    @Test
    fun testAddButtonForTwitterAndFacebookAccount() {
        val email = "accountsTest.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        assertEquals(email, user.email)
        assertEquals(pw, user.password)

        var tHandler = TwitterHandler(context, user)
        tHandler.unlinkAccount()

        activityRule.launchActivity(null)
        onView(withId(R.id.ViewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.addaccount)).check(matches(isDisplayed()))
        onView(withId(R.id.addaccount)).perform(click())

        onView(withId(R.id.addTwitter)).check(matches(isDisplayed()))
        onView(withId(R.id.addFacebook)).check(matches(isDisplayed()))
    }

    @Test
    fun testTwitterHasLinkedAccount() {
        val email = "accountsTest.user@test.com"
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

        activityRule.launchActivity(null)
        onView(withId(R.id.ViewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.rlConnectedTwitterAccount)).check(matches(isDisplayed()))
        onView(withId(R.id.txtTwitterUserName)).check(matches(withText("Test")))
        onView(withId(R.id.removeTwitterbtn)).check(matches(withText(resources.getString(R.string.disconnect_button))))
    }

    @Test
    fun testFacebookHasLinkedAccount() {
        FacebookSdk.setApplicationId("171191854853298")
        val email = "accountsTest.user@test.com"
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

        activityRule.launchActivity(null)
        onView(withId(R.id.ViewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.rlConnectedFacebookAccount)).check(matches(isDisplayed()))
        onView(withId(R.id.txtFacebookUserName)).check(matches(withText("Test")))
        onView(withId(R.id.removeFacebookbtn)).check(matches(withText(resources.getString(R.string.disconnect_button))))
    }
}