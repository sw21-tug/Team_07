package com.example.pangea

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.text.InputType
import android.widget.EditText
import androidx.preference.Preference
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsInstrumentedTest {

    @get:Rule
    val mActivityTestRule: ActivityTestRule<SettingsActivity> =
            object : ActivityTestRule<SettingsActivity>(SettingsActivity::class.java, true, false) {
                override fun getActivityIntent(): Intent {
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    return Intent(targetContext, SettingsActivity::class.java).apply {
                        putExtra("loggedInUserMail", "settingsTestUser")
                    }
                }
            }
    @Before
    fun setUp() {
        val email = "settingsTestUser"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
    }

    @After
    fun cleanUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandlert = com.example.pangea.DatabaseHandler()
        val email = "settingsTestUser"
        dbHandlert.deletUserByEmail(email, context)
    }


    var resources: Resources = InstrumentationRegistry.getInstrumentation().targetContext.resources

    @Test
    fun changePasswordTest() {
        val email = "settingsTestUser"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        mActivityTestRule.launchActivity(null)

        onView(withId(androidx.preference.R.id.recycler_view))
                .perform(actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(R.string.change_password_title)), click()))
        onView(withInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)).perform(typeTextIntoFocusedView("1234"))
        onView(withText("OK")).perform(click())
        user = dbHandler.getRegisteredUser(email, context)
        assert(user.password.toString() == "1234abc")
    }

    @Test
    fun testSwitchLanguage() {
        val email = "settingsTestUser"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        var user = dbHandler.getRegisteredUser(email, context)
        Assert.assertEquals(email, user.email)
        Assert.assertEquals(pw, user.password)

        mActivityTestRule.launchActivity(null)

        onView(withText("Language")).check(matches(isDisplayed()))

        onView(withId(androidx.preference.R.id.recycler_view))
                .perform(actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText("English")), click()))

        val context2 = ApplicationProvider.getApplicationContext<Context>()
        user = dbHandler.getRegisteredUser(email, context2)
        Assert.assertEquals("en", user.language)

        onView(withText("Russian"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        user = dbHandler.getRegisteredUser(email, context)
    }
}