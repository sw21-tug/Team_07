package com.example.pangea

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.test.rule.ActivityTestRule
import com.example.pangea.Posts
import com.example.pangea.R.id.sendpostbtn
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import org.hamcrest.Matchers
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import junit.framework.Assert
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class PostTests {
        @get:Rule var rule = ActivityScenarioRule(RegisterAndLoginActivity::class.java)

        @Before
        fun setUp() {
            Intents.init()
            val email = "postTestUser"
            val contextt = ApplicationProvider.getApplicationContext<Context>()
            val dbHandlert = com.example.pangea.DatabaseHandler()
            dbHandlert.registerUser(email, "postTest342", contextt)
            val register = DatabaseHandler()
            val postslist = register.getAllPosts(email, contextt)
            postslist.forEach{register.deletePostByID(it.postID!!, contextt)}
            val message = "test"
            val image = null
            register.addFBPost(email, message, image, contextt, "")
        }

        @After
        fun cleanUp() {
            Intents.release()
            val contextt = ApplicationProvider.getApplicationContext<Context>()
            val dbHandlert = com.example.pangea.DatabaseHandler()
            dbHandlert.deletUserByEmail("postTestUser", contextt)
        }

        @Test
        fun testButton ()
        {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            Assert.assertEquals("com.example.pangea", appContext.packageName)

            onView(withId(R.id.username)).perform(clearText())
            onView(withId(R.id.username)).perform(typeText("postTestUser"))
            onView(withId(R.id.password)).perform(clearText())
            onView(withId(R.id.password)).perform(typeText("postTest342"))

            onView(withId(R.id.loginButton)).perform(click())

            //check if Dashboard is shown after login
            Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

            onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

            onView(withId(sendpostbtn)).perform(click())
        }

    @Test
    fun testSelectAccount ()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("postTestUser"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("postTest342"))

        onView(withId(R.id.loginButton)).perform(click())


        val emailt = "postTestUser"
        val pwt = "postTest342"
        val contextt = ApplicationProvider.getApplicationContext<Context>()
        val dbHandlert = com.example.pangea.DatabaseHandler()
        var usert = dbHandlert.getRegisteredUser(emailt, contextt)

        val handler = TwitterHandler(contextt, usert)
        var hasAccount = handler.hasLinkedAccount()
        if (hasAccount)
        {
            handler.unlinkAccount()
        }

        FacebookSdk.setApplicationId("171191854853298")
        val email = "postTestUser"
        val pw = "postTest342"
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

        onView(Matchers.allOf(ViewMatchers.withText("BOOKMARKED"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

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
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("postTestUser"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("postTest342"))
        onView(withId(R.id.loginButton)).perform(click())

        val emailt = "postTestUser"
        val pwt = "postTest342"
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
        val email = "postTestUser"
        val pw = "postTest342"
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

        onView(withId(sendpostbtn)).check(matches(not(isEnabled())));
    }

    @Test
    fun testSearchButton ()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("postTestUser"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("postTest342"))

        onView(withId(R.id.loginButton)).perform(click())

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(R.id.searchbtn)).check(matches(isDisplayed()))
    }

    @Test
    fun basicPost() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("postTestUser"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("postTest342"))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        val email = "postTestUser"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "postTest342"
        val image = null

        register.addFBPost(email, message, image, context, "")
        assertEquals("com.example.pangea", appContext.packageName)

        onView(anyOf(withId(R.id.bookmark_checkbox)))
        PostDatabase.destroyInstance()
    }

    @Test
    fun testExpandPost() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("postTestUser"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("postTest342"))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))
        assertEquals("com.example.pangea", appContext.packageName)

        onView(Matchers.allOf(ViewMatchers.withText("ACCOUNTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        onView(anyOf(withId(R.id.post_text_field))).perform(click())
        onView(withId(R.id.TextViewPostExpanded)).check(matches(isDisplayed()))
        PostDatabase.destroyInstance()
    }

    @Test
    fun testLongClickDelete() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("postTestUser"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("postTest342"))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        val email = "postTestUser"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()

        assertEquals("com.example.pangea", appContext.packageName)
        onView(anyOf(withId(R.id.post_text_field))).perform(longClick())

        onView(withText("Delete Post")).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click());

        var postslist = register.getAllPosts(email, context)
        require(postslist.isEmpty())
        PostDatabase.destroyInstance()
    }

}