package com.example.pangea

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsTests {

    @Test
    fun testChangePassword(){

        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)

        Assert.assertEquals(user.password, "1234abc")
        dbHandler.changePassword(email, "1234", context)
        user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(user.password, "1234")
    }

    @Test
    fun testSwitchLanguage(){

        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)

        Assert.assertEquals(user.language, "de")
        dbHandler.updateUserLanguage(user, "ru", context)
        Assert.assertEquals(user.language, "ru")
        dbHandler.updateUserLanguage(user,"de", context)
        Assert.assertEquals(user.language, "de")
    }
}