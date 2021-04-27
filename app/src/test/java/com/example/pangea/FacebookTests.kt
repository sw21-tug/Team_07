package com.example.pangea

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import junit.framework.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class FacebookTests {

    @Test
    fun testFacebookLink() {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        val facebookOauthToken = "testtoken"
        dbHandler.saveFacebookLink(user, facebookOauthToken, context)

        val fbHandler = FacebookHandler(context)
        val hasAccount = fbHandler.hasLinkedAccount()
        assertEquals(hasAccount, true)
    }

    @Test
    fun testUnlinkFacebook() {
        FacebookSdk.setApplicationId("171191854853298")
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        FacebookSdk.sdkInitialize(context)
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        val facebookOauthToken = "testtoken"
        dbHandler.saveFacebookLink(user, facebookOauthToken, context)

        val fbHandler = FacebookHandler(context)
        var hasAccount = fbHandler.hasLinkedAccount()
        assertEquals(hasAccount, true)

        fbHandler.logoutFacebook()
        hasAccount = fbHandler.hasLinkedAccount()
        assertEquals(hasAccount, false)
    }
}