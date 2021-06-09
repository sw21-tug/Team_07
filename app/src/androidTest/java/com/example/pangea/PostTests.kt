package com.example.pangea

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.pangea.Posts
import com.example.pangea.R.id.add_media_btn
import com.example.pangea.R.id.sendpostbtn
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import org.hamcrest.Matchers
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.io.File


@RunWith(AndroidJUnit4::class)
class PostTests {
        @get:Rule var rule = ActivityScenarioRule(RegisterAndLoginActivity::class.java)

        val userEmail = "postTestUser"
        val userPassword = "postTest342"

        @Before
        fun setUp() {
            Intents.init()
            val contextt = ApplicationProvider.getApplicationContext<Context>()
            val dbHandlert = com.example.pangea.DatabaseHandler()
            dbHandlert.registerUser(userEmail, userPassword, contextt)
            val register = DatabaseHandler()
            val postslist = register.getAllPosts(userEmail, contextt)
            postslist.forEach{register.deletePostByID(it.postID!!, contextt)}
            var user = dbHandlert.getRegisteredUser(userEmail, contextt)
            var tHandler = TwitterHandler(contextt, user)
            val twitterAuthToken = "token"
            val twitterAuthTokenSecret = "secret"
            dbHandlert.saveTwitterLink(user, twitterAuthToken, twitterAuthTokenSecret, contextt)
            val message = "test"
            val image = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + contextt.getResources().getResourcePackageName(R.drawable.splash)
                    + '/' + contextt.getResources().getResourceTypeName(R.drawable.splash) + '/' + contextt.getResources().getResourceEntryName(R.drawable.splash))
            register.addFBPost(userEmail, message, image.toString(), contextt, "", "01-01-2021")
        }

        @After
        fun cleanUp() {
            Intents.release()
            val contextt = ApplicationProvider.getApplicationContext<Context>()
            val dbHandlert = com.example.pangea.DatabaseHandler()
            dbHandlert.deletUserByEmail(userEmail, contextt)
        }

        @Test
        fun testButton ()
        {
            Intents.init()
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            assertEquals("com.example.pangea", appContext.packageName)

            onView(withId(R.id.username)).perform(clearText())
            onView(withId(R.id.username)).perform(typeText(userEmail))
            onView(withId(R.id.password)).perform(clearText())
            onView(withId(R.id.password)).perform(typeText(userPassword))

            onView(withId(R.id.loginButton)).perform(click())

            //check if Dashboard is shown after login
            Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

            onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

//            rule.scenario
            onView(withId(sendpostbtn)).perform(click())

            //onView(withId(R.id.facebookCheck)).check(matches(isDisplayed()))
           // onView(withId(R.id.twitterCheck)).check(matches(isDisplayed()))
           // onView(withId(R.id.plain_text_input)).check(matches(isDisplayed()))

//            onView(withId(R.id.facebookCheck)).perform(click())
//            onView(withId(R.id.facebookCheck)).check(matches(isChecked()))
//
//            onView(withId(R.id.twitterCheck)).perform(click())
//            onView(withId(R.id.twitterCheck)).check(matches(isChecked()))
        }

    @Test
    fun testSelectAccount ()
    {
        //Intents.init()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())


        val emailt = userEmail
        val pwt = userPassword
        val contextt = ApplicationProvider.getApplicationContext<Context>()
        val dbHandlert = com.example.pangea.DatabaseHandler()
        dbHandlert.registerUser(emailt, pwt, contextt)
        var usert = dbHandlert.getRegisteredUser(emailt, contextt)

        val handler = TwitterHandler(contextt, usert)
        var hasAccount = handler.hasLinkedAccount()
        if (hasAccount)
        {
            handler.unlinkAccount()
        }

        FacebookSdk.setApplicationId("171191854853298")
        val email = userEmail
        val pw = userPassword
        val context = ApplicationProvider.getApplicationContext<Context>()
        FacebookSdk.sdkInitialize(context)
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)

        val facebookOauthToken = "testtoken"
        dbHandler.saveFacebookLink(user, facebookOauthToken, context)

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))


        onView(withId(sendpostbtn)).perform(click())

        onView(withId(R.id.facebookCheck)).check(matches(isDisplayed()))
        onView(withId(R.id.twitterCheck)).check(matches(isDisplayed()))
        onView(withId(R.id.plain_text_input)).check(matches(isDisplayed()))

        onView(withId(R.id.facebookCheck)).perform(click())
        onView(withId(R.id.facebookCheck)).check(matches(isChecked()))

        onView(withId(R.id.twitterCheck)).perform(click())
        onView(withId(R.id.twitterCheck)).check(matches(isNotChecked()))
    }

    @Test
    fun testPlusButtonNoAcc ()
    {
        //Intents.init()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))
        onView(withId(R.id.loginButton)).perform(click())

        val emailt = userEmail
        val pwt = userPassword
        val contextt = ApplicationProvider.getApplicationContext<Context>()
        val dbHandlert = com.example.pangea.DatabaseHandler()
        dbHandlert.registerUser(emailt, pwt, contextt)
        var usert = dbHandlert.getRegisteredUser(emailt, contextt)

        val thandler = TwitterHandler(contextt, usert)
        var thasAccount = thandler.hasLinkedAccount()
        if (thasAccount)
        {
            thandler.unlinkAccount()
        }

        FacebookSdk.setApplicationId("171191854853298")
        val email = userEmail
        val pw = userPassword
        val context = ApplicationProvider.getApplicationContext<Context>()
        FacebookSdk.sdkInitialize(context)
        val dbHandler = DatabaseHandler()
        dbHandler.registerUser(email, pw, context)
        var user = dbHandler.getRegisteredUser(email, context)
        junit.framework.Assert.assertEquals(email, user.email)
        junit.framework.Assert.assertEquals(pw, user.password)


        dbHandler.saveFacebookLink(user, null, context)
        if(AccessToken.getCurrentAccessToken() != null)
        {
            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/", null, HttpMethod.DELETE,
                GraphRequest.Callback {
                    AccessToken.setCurrentAccessToken(null);
                    LoginManager.getInstance().logOut()
                })
        }

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(sendpostbtn)).check(matches(not(isEnabled())));
    }

    @Test
    fun testSearchButton ()
    {
        //Intents.init()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(R.id.searchbtn)).check(matches(isDisplayed()));
    }

    @Test
    fun basicPost() {

        Intents.init()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        assertEquals("com.example.pangea", appContext.packageName)

        onView(anyOf(withId(R.id.bookmark_checkbox)))

        PostDatabase.destroyInstance()
    }

    @Test
    fun testExpandPost() {

        Intents.init()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(anyOf(withId(R.id.post_text_field))).perform(click())

       //Intents.intended(IntentMatchers.hasComponent(PostExpanded::class.java.name))
        onView(withId(R.id.TextViewPostExpanded)).check(matches(isDisplayed()))

        PostDatabase.destroyInstance()
    }

    @Test
    fun testLongClickDelete() {

        Intents.init()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        val email = userEmail
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = null

        assertEquals("com.example.pangea", appContext.packageName)

        onView(anyOf(withId(R.id.post_text_field))).perform(longClick())

        onView(withText("Delete Post")).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click());

        postslist = register.getAllPosts(email, context)

        require(postslist.isEmpty())

        PostDatabase.destroyInstance()
    }

    @Test
    fun testFilterPosts() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
      
        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))
        
        val email = userEmail
        val dbhandler = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val image = ""

        val postslist = dbhandler.getAllPosts(userEmail, context)

        postslist.forEach{dbhandler.deletePostByID(it.postID!!, context)}

        dbhandler.addFBPost(email, "testmessage 1", image, context, "", "21-05-2021")
        dbhandler.addFBPost(email, "testmessage 2", image, context, "", "26-05-2021")
        dbhandler.addTwitterPost(email, "testmessage 3", image, context, "", "24-05-2021")
        dbhandler.addTwitterPost(email, "testmessage 4", image, context, "", "26-05-2021")

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
                .perform(click())
                .check(matches(isDisplayed()))

        assertEquals("com.example.pangea", appContext.packageName)

        onView(withId(R.id.searchbtn)).perform(click())
        onView(anyOf(withId(R.id.filter_post_content))).perform(typeText("testmessage 1"))
        onView(anyOf(withId(R.id.filter_post_content))).perform(pressBack())
        onView(anyOf(withId(R.id.btn_set_filter))).perform(click())
        onView(withText("testmessage 1")).check(matches(isDisplayed()))

        onView(withId(R.id.searchbtn)).perform(click())
        onView(anyOf(withId(R.id.toggleByDate))).perform(click())
        onView(anyOf(withId(R.id.dpFilter))).perform(PickerActions.setDate(2021, 5, 26))
        onView(anyOf(withId(R.id.btn_set_filter))).perform(click())
        onView(withText("testmessage 2")).check(matches(isDisplayed()))
        onView(withText("testmessage 4")).check(matches(isDisplayed()))

        onView(withId(R.id.searchbtn)).perform(click())
        onView(anyOf(withId(R.id.toggleByPlatform))).perform(click())
        onView(anyOf(withId(R.id.rb_filter_facebook))).perform(click())
        onView(anyOf(withId(R.id.btn_set_filter))).perform(click())
        onView(withText("testmessage 1")).check(matches(isDisplayed()))
        onView(withText("testmessage 2")).check(matches(isDisplayed()))

        onView(withId(R.id.searchbtn)).perform(click())
        onView(anyOf(withId(R.id.toggleByPlatform))).perform(click())
        onView(anyOf(withId(R.id.rb_filter_twitter))).perform(click())
        onView(anyOf(withId(R.id.btn_set_filter))).perform(click())
        onView(withText("testmessage 3")).check(matches(isDisplayed()))
        onView(withText("testmessage 4")).check(matches(isDisplayed()))

        postslist.forEach{dbhandler.deletePostByID(it.postID!!, context)}
        onView(withId(R.id.searchbtn)).perform(click())
        onView(anyOf(withId(R.id.btn_set_filter))).perform(click())

        PostDatabase.destroyInstance()
    }

    @Test
    fun testFeedback() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val email = userEmail
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"

        register.deleteAllPosts(appContext)
        register.addFBPost(email, message, "", context, "", "08-06-2021")

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(anyOf(withId(R.id.post_text_field))).perform(click())
        onView(withId(R.id.TextViewPostExpanded)).check(matches(isDisplayed()))

        onView(withId(R.id.FacebookLikes)).check(matches(isDisplayed()))
        onView(withId(R.id.TwitterLikes)).check(matches(not(isDisplayed())))

        PostDatabase.destroyInstance()
    }
    @Test
    fun testIfMediaButtonVisible() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        val email = userEmail
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = null


        onView(anyOf(withId(R.id.sendpostbtn))).perform(click())

        onView(withId(R.id.add_media_btn)).check(matches(isDisplayed()))

        PostDatabase.destroyInstance()
    }

    @Test
    fun testMediaButton() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(anyOf(withId(R.id.sendpostbtn))).perform(click())

        onView(withId(R.id.preview_picture)).check(matches(isDisplayed()))

        PostDatabase.destroyInstance()

    }

    @Test
    fun ImagePost() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        val email = userEmail
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test with image"

        val image = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(R.drawable.splash)
                + '/' + context.getResources().getResourceTypeName(R.drawable.splash) + '/' + context.getResources().getResourceEntryName(R.drawable.splash))

        var postslist = register.getAllPosts(email, context)

        postslist.forEach{register.deletePostByID(it.postID!!, context)}

        assertFalse(image == null)

        register.addFBPost(email, message, image.toString(), context, "", "08-06-2021")
        assertEquals("com.example.pangea", appContext.packageName)

        onView(anyOf(withId(R.id.bookmark_checkbox)))



        PostDatabase.destroyInstance()
    }

    @Test
    fun testExpandPostWithImage() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.username)).perform(typeText(userEmail))
        onView(withId(R.id.password)).perform(clearText())
        onView(withId(R.id.password)).perform(typeText(userPassword))

        onView(withId(R.id.loginButton)).perform(click())

        //check if Dashboard is shown after login
        Intents.intended(IntentMatchers.hasComponent(DashboardsActivity::class.java.name))

        onView(Matchers.allOf(ViewMatchers.withText("POSTS"), ViewMatchers.isDescendantOfA(withId(R.id.dashboard_bar))))
            .perform(click())
            .check(matches(isDisplayed()))

        val email = userEmail
        val register = DatabaseHandler()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "test"
        val image = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(R.drawable.splash)
                + '/' + context.getResources().getResourceTypeName(R.drawable.splash) + '/' + context.getResources().getResourceEntryName(R.drawable.splash))

        val postslist = register.getAllPosts(userEmail, context)

        postslist.forEach{register.deletePostByID(it.postID!!, context)}

        register.addFBPost(email, message, image.toString(), context, "", "08-06-2021")
        assertEquals("com.example.pangea", appContext.packageName)

        //onView(withId(R.id.refresh)).perform(click())
        onView(anyOf(withId(R.id.post_text_field))).perform(click())

        //Intents.intended(IntentMatchers.hasComponent(PostExpanded::class.java.name))
        onView(withId(R.id.ImagePostExpanded)).check(matches(isDisplayed()))

        PostDatabase.destroyInstance()
    }

}