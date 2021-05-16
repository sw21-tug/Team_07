package com.example.pangea

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pangea.Posts
import com.example.pangea.R.id.sendpostbtn
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class PostTests {
        @get:Rule var rule = ActivityScenarioRule(RegisterAndLoginActivity::class.java)

        @Test
        fun testButton ()
        {
            Intents.init()
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            Assert.assertEquals("com.example.pangea", appContext.packageName)

            onView(withId(R.id.username)).perform(clearText())
            onView(withId(R.id.username)).perform(typeText("test"))
            onView(withId(R.id.password)).perform(clearText())
            onView(withId(R.id.password)).perform(typeText("test"))

            onView(withId(R.id.loginButton)).perform(click())

            //check if Dashboard is shown after login
            Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

            onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

//            rule.scenario
            onView(withId(sendpostbtn)).perform(click())

            onView(withId(R.id.facebookCheck)).check(matches(isDisplayed()))
            onView(withId(R.id.twitterCheck)).check(matches(isDisplayed()))
            onView(withId(R.id.plain_text_input)).check(matches(isDisplayed()))

//            onView(withId(R.id.facebookCheck)).perform(click())
//            onView(withId(R.id.facebookCheck)).check(matches(isChecked()))
//
//            onView(withId(R.id.twitterCheck)).perform(click())
//            onView(withId(R.id.twitterCheck)).check(matches(isChecked()))
        }

    @Test
    fun testSelectAccount ()
    {
        //Intents.init()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("test"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("test"))

        onView(withId(R.id.loginButton)).perform(click())


        val emailt = "test.user@test.com"
        val pwt = "1234abc"
        val contextt = ApplicationProvider.getApplicationContext<Context>()
        val dbHandlert = com.example.pangea.DatabaseHandler()
        dbHandlert.registerUser(emailt, pwt, contextt)
        var usert = dbHandlert.getRegisteredUser(emailt, contextt)

        val handler = TwitterHandler(contextt, usert)
        var hasAccount = handler.hasLinkedAccount()
        if (hasAccount)
        {
            handler.unlinkAccount()
        }

        FacebookSdk.setApplicationId("171191854853298")
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        FacebookSdk.sdkInitialize(context)
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        junit.framework.Assert.assertEquals(email, user.email)
        junit.framework.Assert.assertEquals(pw, user.password)

        val facebookOauthToken = "testtoken"
        dbHandler.saveFacebookLink(user, facebookOauthToken, context)

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(sendpostbtn)).perform(click())

        onView(withId(R.id.facebookCheck)).check(matches(isDisplayed()))
        onView(withId(R.id.twitterCheck)).check(matches(isDisplayed()))
        onView(withId(R.id.plain_text_input)).check(matches(isDisplayed()))

        onView(withId(R.id.facebookCheck)).perform(click())
        onView(withId(R.id.facebookCheck)).check(matches(isChecked()))

        onView(withId(R.id.twitterCheck)).perform(click())
        onView(withId(R.id.twitterCheck)).check(matches(isNotChecked()))
    }

    @Test
    fun testPlusButtonNoAcc ()
    {
        //Intents.init()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("test"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("test"))

        onView(withId(R.id.loginButton)).perform(click())


        val emailt = "test.user@test.com"
        val pwt = "1234abc"
        val contextt = ApplicationProvider.getApplicationContext<Context>()
        val dbHandlert = com.example.pangea.DatabaseHandler()
        dbHandlert.registerUser(emailt, pwt, contextt)
        var usert = dbHandlert.getRegisteredUser(emailt, contextt)

        val thandler = TwitterHandler(contextt, usert)
        var thasAccount = thandler.hasLinkedAccount()
        if (thasAccount)
        {
            thandler.unlinkAccount()
        }

        FacebookSdk.setApplicationId("171191854853298")
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        FacebookSdk.sdkInitialize(context)
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        junit.framework.Assert.assertEquals(email, user.email)
        junit.framework.Assert.assertEquals(pw, user.password)


        dbHandler.saveFacebookLink(user, null, context)
        if(AccessToken.getCurrentAccessToken() != null)
        {
            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/", null, HttpMethod.DELETE,
                GraphRequest.Callback {
                    AccessToken.setCurrentAccessToken(null);
                    LoginManager.getInstance().logOut()
                })
        }

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(sendpostbtn)).check(matches(not(isClickable())));
    }

}