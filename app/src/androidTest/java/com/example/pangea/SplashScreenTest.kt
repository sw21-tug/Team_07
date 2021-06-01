package com.example.pangea

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest{
    @get:Rule var rule = ActivityTestRule<SplashActivity>(SplashActivity::class.java, true, false)

    @Before
    fun setup(){
        Intents.init()
    }

    @Test
    fun testSplashScreen() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.pangea", appContext.packageName)
        rule.launchActivity(null)
        intended(hasComponent(SplashActivity::class.java!!.getName()))

        Thread.sleep(2500)
        intended(hasComponent(RegisterAndLoginActivity::class.java!!.getName()))
    }
}