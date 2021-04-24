package com.example.pangea

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class FacebookTests {

    @Test
    fun testFacebookLink() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val pref = context.applicationContext.getSharedPreferences("facebook", Context.MODE_PRIVATE)
        pref.edit().putString("facebook_oauth_token", "testtoken").apply()

        val fhandler = FacebookHandler(context)
        val hasAccount = fhandler.hasLinkedAccount()
        assertEquals(hasAccount, true)
    }

    @Test
    fun testUnlinkFacebook() {
        FacebookSdk.setApplicationId("171191854853298")
        val context = ApplicationProvider.getApplicationContext<Context>()
        FacebookSdk.sdkInitialize(context)
        val pref = context.applicationContext.getSharedPreferences("facebook", Context.MODE_PRIVATE)
        pref.edit().putString("facebook_oauth_token", "testtoken").apply()

        val fhandler = FacebookHandler(context)
        var hasAccount = fhandler.hasLinkedAccount()
        assertEquals(hasAccount, true)


        fhandler.logoutFacebook()
        hasAccount = fhandler.hasLinkedAccount()
        assertEquals(hasAccount, false)
    }
}