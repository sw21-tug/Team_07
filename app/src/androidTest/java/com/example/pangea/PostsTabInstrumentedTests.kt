package com.example.pangea

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView

import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.pangea.Posts
import junit.framework.Assert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PostsTabInstrumentedTests {
    // launch desired activity
    @get:Rule
    val mActivityTestRule: ActivityTestRule<DashboardsActivity> =
            object : ActivityTestRule<DashboardsActivity>(DashboardsActivity::class.java, true, false) {
                override fun getActivityIntent(): Intent {
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    val sharedPref = targetContext.getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("current_user","postTestUser").apply()
                    return Intent(targetContext, DashboardsActivity::class.java)
                }
            }

    @Before
    fun setupUser()
    {
        val email = "postTestUser"
        val contextt = ApplicationProvider.getApplicationContext<Context>()
        val dbHandlert = com.example.pangea.DatabaseHandler()
        dbHandlert.registerUser(email, "postTest342", contextt)
        val register = DatabaseHandler()
        val postslist = register.getAllPosts(email, contextt)
        postslist.forEach{register.deletePostByID(it.postID!!, contextt)}
        val message = "test"
        val image = null
        //register.addFBPost(email, message, image, contextt, "")
    }

    @After
    fun cleanUp() {
        val contextt = ApplicationProvider.getApplicationContext<Context>()
        val dbHandlert = com.example.pangea.DatabaseHandler()
        dbHandlert.deletUserByEmail("postTestUser", contextt)
    }

    @Test
    fun basicPost() {
        val email = "postTestUser"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = null

        register.addFBPost(email, message, image, context, "", "26-05-2021")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        mActivityTestRule.launchActivity(null)

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(isDisplayed()))
        // if we only inserted one post, only one post should be displayed in the posts tab
        onView(withId(R.id.linearLayoutPosts)).check(ViewAssertions.matches(hasChildCount(1)))

        register.deletePost(email, message, image, "Facebook", context)
        PostDatabase.destroyInstance()
    }

    @Test
    fun multiplePosts() {

        val email = "postTestUser"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val messageFB = "testFB"
        val messageTwitter = "testTwitter"
        val image = null
        register.addFBPost(email, messageFB, image, context, "", "26-05-2021")
        register.addTwitterPost(email, messageTwitter, image, context, "", "26-05-2021")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        mActivityTestRule.launchActivity(null)

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.linearLayoutPosts)).check(ViewAssertions.matches(hasChildCount(2)))

        register.deletePost(email, messageFB, image, "Facebook", context)
        register.deletePost(email, messageTwitter, image, "Twitter", context)

        PostDatabase.destroyInstance()
    }



    @After
    fun destroyData()
    {
        AppDatabase.destroyInstance()
    }
}