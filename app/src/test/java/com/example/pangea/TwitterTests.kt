package com.example.pangea

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import junit.framework.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TwitterTests {

    @Test
    fun testTwitterLink() {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = com.example.pangea.DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        val twitterAuthToken = "token"
        val twitterAuthTokenSecret = "secret"
        dbHandler.saveTwitterLink(user, twitterAuthToken, twitterAuthTokenSecret, context)

        val handler = TwitterHandler(context)
        val hasAccount = handler.hasLinkedAccount()
        assertEquals(hasAccount, true)
    }

    @Test
    fun testUnlinkTwitter() {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = com.example.pangea.DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        val twitterAuthToken = "token"
        val twitterAuthTokenSecret = "secret"
        dbHandler.saveTwitterLink(user, twitterAuthToken, twitterAuthTokenSecret, context)

        val handler = TwitterHandler(context)
        var hasAccount = handler.hasLinkedAccount()
        assertEquals(hasAccount, true)

        handler.unlinkAccount()
        hasAccount = handler.hasLinkedAccount()
        assertEquals(hasAccount, false)
    }

    @Test
    fun testFailPostTwitterStatus() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val pref = context.applicationContext.getSharedPreferences("twitter", Context.MODE_PRIVATE)
        pref.edit().putString("twitter_oauth_token", "test-test").apply()
        pref.edit().putString("twitter_oauth_token_secret", "test-test").apply()

        val handler = TwitterHandler(context)
        var hasAccount = handler.hasLinkedAccount()
        assertEquals(hasAccount, true)

        val response  = handler.postTweet("Test Tweet")
        assertEquals(response, "-1")
    }
}