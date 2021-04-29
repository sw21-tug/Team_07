package com.example.pangea

import android.content.Context
import android.content.Intent
import androidx.room.Database
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.pangea.Posts
import junit.framework.Assert
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.util.regex.Pattern.matches

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
                    return Intent(targetContext, DashboardsActivity::class.java).apply {
                        putExtra("loggedInUserMail", "test.user@test.com")
                    }
                }
            }

    @Before
    fun setupUser()
    {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        DatabaseHandler().registerUser(email, pw, context)
    }

    @Test
    fun basicPost() {
        val email = "test.user@test.com"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = null

        register.addFBPost(email, message, image, context, "")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        mActivityTestRule.launchActivity(null)
        // if we only inserted one post, only one post should be displayed in the posts tab
        onView(withId(R.id.linearLayout)).check(ViewAssertions.matches(hasChildCount(1)))

        register.deletePost(email, message, image, "Facebook", context)
        PostDatabase.destroyInstance()
    }

    @Test
    fun multiplePosts() {

        val email = "test.user@test.com"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val messageFB = "testFB"
        val messageTwitter = "testTwitter"
        val image = null
        register.addFBPost(email, messageFB, image, context, "")
        register.addTwitterPost(email, messageTwitter, image, context, "")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        mActivityTestRule.launchActivity(null)
        // if we only inserted one post, only one post should be displayed in the posts tab
        onView(withId(R.id.linearLayout)).check(ViewAssertions.matches(hasChildCount(2)))

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