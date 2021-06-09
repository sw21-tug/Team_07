package com.example.pangea

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PostsTabTests {

    @After
    fun tearDown() {
        PostDatabase.destroyInstance()
    }

    @Test
    fun testPostData() {

        val email = "test.user@test.com"
        val register = com.example.pangea.DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = null
        register.addFBPost(email, message, image, context, "", "26-05-2021")

        val posts = register.getAllPosts(email, context)
        assertEquals(1, posts.size)
    }

    @Test
    fun testFilterPost(){

        val email = "test.user@test.com"
        val dbhandler = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbhandler.addFBPost(email, "testmessage 1", null, context, "", "21-05-2021")
        dbhandler.addFBPost(email, "testmessage 2", null, context, "", "26-05-2021")
        dbhandler.addTwitterPost(email, "testmessage 3", null, context, "", "24-05-2021")
        dbhandler.addTwitterPost(email, "testmessage 4", null, context, "", "26-05-2021")

        var posts = dbhandler.getAllPosts(email, context)
        assertEquals(4, posts.size)

        var filterPosts = dbhandler.filterPostsByContent(email, context, "testmessage 1")
        assertEquals(1, filterPosts?.size)

        filterPosts = dbhandler.filterPostsByContent(email, context, "not found!")
        assertEquals(0, filterPosts?.size)

        filterPosts = dbhandler.filterPostsByPlatform(email, context, true)
        assertEquals(2, filterPosts?.size)

        filterPosts = dbhandler.filterPostsByPlatform(email, context, false)
        assertEquals(2, filterPosts?.size)

        filterPosts = dbhandler.filterPostsByDate(email, context, "26-05-2021")
        assertEquals(2, filterPosts?.size)

        filterPosts = dbhandler.filterPostsByDate(email, context, "21-05-2021")
        assertEquals(1, filterPosts?.size)
    }
}