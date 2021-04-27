package com.example.pangea

import android.content.Context
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pangea.Posts
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
    @get:Rule var rule = ActivityScenarioRule(DashboardsActivity::class.java)

    @Test
    fun basicPost() {
        rule.scenario
        val email = "test.user@test.com"
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = null
        register.addFBPost(email, message, image, context)
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        // if we only inserted one post, only one post should be displayed in the posts tab
        onView(withId(R.id.linearLayout)).check(ViewAssertions.matches(hasChildCount(1)))

        register.deletePost(email, message, image, "Facebook", context)
        PostDatabase.destroyInstance()
    }
}