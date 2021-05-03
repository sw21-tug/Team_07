package com.example.pangea

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.text.InputType
import android.widget.EditText
import androidx.preference.Preference
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test

class SettingsInstrumentedTest {

    @get:Rule
    val mActivityTestRule: ActivityTestRule<SettingsActivity> =
            object : ActivityTestRule<SettingsActivity>(SettingsActivity::class.java, true, false) {
                override fun getActivityIntent(): Intent {
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    return Intent(targetContext, SettingsActivity::class.java).apply {
                        putExtra("loggedInUserMail", "test.user@test.com")
                    }
                }
            }

    var resources: Resources = InstrumentationRegistry.getInstrumentation().targetContext.resources

    @Test
    fun changePasswordTest() {
        val email = "test.user@test.com"
        val pw = "1234abc"
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
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
        assert(user.password.toString() == "1234")
    }
}