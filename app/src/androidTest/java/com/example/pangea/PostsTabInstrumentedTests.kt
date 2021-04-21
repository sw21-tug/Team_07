package com.example.pangea

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pangea.Posts
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PostsTabInstrumentedTests {

    @Test
    fun basicPost() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        val scenario = launchFragmentInContainer<Posts>()
        val posts = scenario.onFragment(fetchPosts())
        assertEquals(true, posts.size())
    }

}