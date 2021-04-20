package com.example.pangea

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TwitterTests {

    @Test
    fun testTwitterLink() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val pref = context.applicationContext.getSharedPreferences("twitter", Context.MODE_PRIVATE)
        pref.edit().putString("twitter_oauth_token", "test-test").apply()
        pref.edit().putString("twitter_oauth_token_secret", "test-test").apply()

        val handler = TwitterHandler(context)
        val hasAccount = handler.hasLinkedAccount()
        assertEquals(hasAccount, true)
    }

    fun testUnlinkTwitter() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val pref = context.applicationContext.getSharedPreferences("twitter", Context.MODE_PRIVATE)
        pref.edit().putString("twitter_oauth_token", "test-test").apply()
        pref.edit().putString("twitter_oauth_token_secret", "test-test").apply()

        val handler = TwitterHandler(context)
        var hasAccount = handler.hasLinkedAccount()
        assertEquals(hasAccount, true)

        handler.unlinkAccount()
        hasAccount = handler.hasLinkedAccount()
        assertEquals(hasAccount, false)
    }
}