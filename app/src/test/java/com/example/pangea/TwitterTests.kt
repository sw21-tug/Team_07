package com.example.pangea

import org.junit.Test

import org.junit.Assert.*

class TwitterTests {

    @Test
    fun testTwitterLink() {

        val email = "team7mobileapps@gmail.com"
        val password = "team7\$123"
        val twitterHandler = TwitterHandler(email, password)
        handler.linkTwitter();
        assertEquals(true, handler.hasLinkedAccount());
    }
}