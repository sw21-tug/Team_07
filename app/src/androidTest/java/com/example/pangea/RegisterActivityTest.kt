package com.example.pangea

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest{
    @get:Rule var rule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerUser() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText("max.mustermann@test.com"))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText("12345"))

        onView(withId(R.id.registerButton)).perform(click())
    }
}