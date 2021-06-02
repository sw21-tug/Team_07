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
class BookmarkedIntrumentedTest {
        @get:Rule var rule = ActivityScenarioRule(RegisterAndLoginActivity::class.java)

        @Before
        fun setUp() {
            Intents.init()
            val email = "bookmarkedTestUser"
            val contextt = ApplicationProvider.getApplicationContext<Context>()
            val dbHandlert = com.example.pangea.DatabaseHandler()
            dbHandlert.registerUser(email, "bookmarkedTest342", contextt)
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
            dbHandlert.deletUserByEmail("bookmarkedTestUser", contextt)
        }

    @Test
    fun basicPostBookmark() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("bookmarkedTestUser"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("bookmarkedTest342"))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        val email = "bookmarkedTestUser"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "postTest342"
        val image = null

        register.addFBPost(email, message, image, context, "")
        assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.bookmark_checkbox)).perform(click())
        onView(Matchers.allOf(ViewMatchers.withText("BOOKMARKED"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        onView(anyOf(withId(R.id.post_text_field)))
        PostDatabase.destroyInstance()
    }

    @Test
    fun switchBookmark() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("bookmarkedTestUser"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("bookmarkedTest342"))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        val email = "bookmarkedTestUser"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "postTest342"
        val image = null

        register.addFBPost(email, message, image, context, "")
        assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.bookmark_checkbox)).perform(click())
        onView(Matchers.allOf(ViewMatchers.withText("BOOKMARKED"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        onView(anyOf(withId(R.id.post_text_field)))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_checkbox)).perform(click())

        onView(Matchers.allOf(ViewMatchers.withText("BOOKMARKED"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        onView(not(withId(R.id.post_text_field)))

        PostDatabase.destroyInstance()
    }

}