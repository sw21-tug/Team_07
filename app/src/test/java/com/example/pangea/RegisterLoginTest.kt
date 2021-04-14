package com.example.pangea

import junit.framework.Assert
import org.junit.Test

class RegisterLoginTest {

    @Test
    fun registerNewUserTest() {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val wasInserted = com.example.pangea.User.insertNewUser(email, pw)
        Assert.assertEquals(true, wasInserted)
    }
}