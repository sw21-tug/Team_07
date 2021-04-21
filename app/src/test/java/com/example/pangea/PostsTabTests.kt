package com.example.pangea

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import junit.framework.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PostsTabTests {

    @Test
    fun testPostData() {

        val email = "test.user@test.com"
        val register = com.example.pangea.DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = null
        val platforms = listOf(true, false)
        register.addPost(email, message, image, platforms, context)

        val posts = register.getAllPosts(email, context)
        assertEquals(1, posts.size)
    }
}