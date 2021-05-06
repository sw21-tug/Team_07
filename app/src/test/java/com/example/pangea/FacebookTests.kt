package com.example.pangea

import android.accounts.Account
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.facebook.FacebookSdk
import junit.framework.Assert
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class FacebookTests {

    @After
    fun tearDown() {
        AppDatabase.destroyInstance()
    }

    @Test
    fun testFacebookLink() {
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

//        val fbHandler = FacebookHandler(context, user, activity)
//        val hasAccount = fbHandler.hasLinkedAccount()
//        assertEquals(hasAccount, true)
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

//        val fbHandler = FacebookHandler(context, user, activity)
//        var hasAccount = fbHandler.hasLinkedAccount()
//        assertEquals(hasAccount, true)

//        fbHandler.logoutFacebook()
//        hasAccount = fbHandler.hasLinkedAccount()
//        assertEquals(hasAccount, false)
    }
}