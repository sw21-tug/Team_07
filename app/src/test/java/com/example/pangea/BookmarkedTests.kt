package com.example.pangea

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BookmarkedTests {

    @After
    fun tearDown() {
        PostDatabase.destroyInstance()
    }

    @Test
    fun testBookmarkedPost() {

        val email = "test.user@test.com"
        val register = com.example.pangea.DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = null
        register.addFBPost(email, message, image, context, "123")

        register.updatePostBookmarked("123", true, context)
        val bookmarked_posts = register.getAllBookmarkedPosts(email, context)
        Assert.assertEquals(1, bookmarked_posts.size)
    }
}