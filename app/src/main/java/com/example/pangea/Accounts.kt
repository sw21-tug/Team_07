
package com.example.pangea

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.fragment.app.DialogFragment
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.facebook.FacebookSdk
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.account_view.*
import com.facebook.login.widget.LoginButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import twitter4j.Twitter
import kotlin.system.exitProcess

/* This class controls the logic in the "Accounts"-Tab
   New Methods can be implemented as needed.
   Layout-File: account_view.xml */
class Accounts() : DialogFragment(), TwitterHandler.ITwitterCallback, FacebookHandler.IFacebookCallback
{
    //creates the view (account_view.xml)
    lateinit var twitterDialog: Dialog
    lateinit var tHandler: TwitterHandler
    lateinit var twitter_login_btn: Button
    lateinit var fHandler: FacebookHandler
    lateinit var login_button_facebook: Button
    lateinit var add_account_button: FloatingActionButton
    lateinit var account_view: View
    lateinit var hidden_facebook_button: LoginButton
    lateinit var rlLayoutTwitter: RelativeLayout
    lateinit var rlLayoutFacebook: RelativeLayout
    lateinit var twitterUserName: TextView
    lateinit var facebookUserName: TextView
    lateinit var disconnectFacebook: Button
    lateinit var disconnectTwitter: Button


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(context);
        account_view = inflater.inflate(R.layout.account_view, container, false)
        val sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        val userMail = sharedPref.getString("current_user", "").toString()

        val user = DatabaseHandler().getRegisteredUser(userMail, requireContext())

        val context = activity as AppCompatActivity
        tHandler = TwitterHandler(context, user)
        tHandler.initTwitterApi()
        twitter_login_btn = account_view.findViewById(R.id.twitter_login_btn)
        login_button_facebook = account_view.findViewById(R.id.login_button_facebook)
        hidden_facebook_button = account_view.findViewById(R.id.hidden_facebook_button)

        rlLayoutFacebook = account_view.findViewById(R.id.rlConnectedFacebookAccount)
        rlLayoutTwitter = account_view.findViewById(R.id.rlConnectedTwitterAccount)

        facebookUserName = account_view.findViewById(R.id.txtFacebookUserName)
        twitterUserName = account_view.findViewById(R.id.txtTwitterUserName)

        disconnectFacebook = account_view.findViewById(R.id.removeFacebookbtn)
        disconnectTwitter = account_view.findViewById(R.id.removeTwitterbtn)

        add_account_button = account_view.findViewById(R.id.addaccount)

        val addTwitter = account_view.findViewById<FloatingActionButton>(R.id.addTwitter)
        val addFacebook = account_view.findViewById<FloatingActionButton>(R.id.addFacebook)

        add_account_button.setOnClickListener {

            if(addTwitter.visibility == View.VISIBLE){
                addFacebook.visibility = View.GONE
                addTwitter.visibility  = View.GONE
                add_account_button.setImageResource(R.drawable.ic_baseline_add_24)
            }
            else {
                addTwitter.visibility = View.VISIBLE
                addFacebook.visibility = View.VISIBLE
                add_account_button.setImageResource(R.drawable.ic_baseline_close_24)
            }
        }


        addTwitter.setOnClickListener {
            twitter_login_btn.performClick()
            addTwitter.visibility = View.GONE
            addFacebook.visibility = View.GONE
            add_account_button.setImageResource(R.drawable.ic_baseline_add_24)
        }


        addFacebook.setOnClickListener {
            login_button_facebook.performClick()

            addTwitter.visibility = View.GONE
            addFacebook.visibility = View.GONE
            add_account_button.setImageResource(R.drawable.ic_baseline_add_24)
        }



        if(tHandler.hasLinkedAccount()) {
            twitter_login_btn.text = getString(R.string.twitter_unlink_text)
        }
        else{
            twitter_login_btn.text = getString(R.string.twitter_link_text)
        }

        twitter_login_btn.setOnClickListener {
            if(tHandler.hasLinkedAccount()) {
                //clear before connected account from view
                add_account_button.isClickable = true

                //now unlink account
                tHandler.unlinkAccount()
                twitter_login_btn.text =getString(R.string.twitter_link_text)
            }
            else {
                setupDialogAndRequestAuthToken()
            }

        }

        fHandler = FacebookHandler(context, user, activity)

        fHandler.initApi(this)
        login_button_facebook = account_view.findViewById(R.id.login_button_facebook)
        hidden_facebook_button = account_view.findViewById(R.id.hidden_facebook_button)
        hidden_facebook_button.setPermissions("pages_show_list", "public_profile");

        if(fHandler.hasLinkedAccount())
        {
            login_button_facebook.text = getString(R.string.facebook_unlink_text)
        }
        else
        {
            login_button_facebook.text = getString(R.string.facebook_link_text)
        }
        login_button_facebook.setOnClickListener {
            hidden_facebook_button.performClick()
        }

        hidden_facebook_button.setOnClickListener {
            fHandler.loginFacebook();
        }

        disconnectFacebook.setOnClickListener {
            rlLayoutFacebook.visibility = View.GONE
            fHandler.logoutFacebook()
            add_account_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
            add_account_button.isClickable = true
        }

        disconnectTwitter.setOnClickListener {
            rlLayoutTwitter.visibility = View.GONE
            tHandler.unlinkAccount()
            add_account_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
            add_account_button.isClickable = true
        }
        refreshConnectedAccounts(false)
        if(rlLayoutFacebook.visibility == View.VISIBLE &&  rlLayoutTwitter.visibility == View.VISIBLE){
            add_account_button.isClickable = false
            add_account_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        }
        return account_view
    }

    private fun setupDialogAndRequestAuthToken() {
        GlobalScope.launch(Dispatchers.Default) {

            try {
                val requestToken = tHandler.getTwitterConnector()?.oAuthRequestToken
                withContext(Dispatchers.Main) {
                    if (requestToken != null) {
                        setupTwitterWebviewDialog(requestToken.authorizationURL + "&force_login=true")
                        twitterDialog.show()
                    }
                }
            } catch (e: IllegalStateException) {
                Log.e("ERROR: ", e.toString())
            }
        }
    }

    private fun setupTwitterWebviewDialog(url: String): Dialog {
        twitterDialog = Dialog(requireContext())
        val webView = WebView(activity?.applicationContext!!)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = tHandler.TwitterWebViewClient(this)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        twitterDialog.setContentView(webView)
        return twitterDialog

    }

    override fun oAuthResponse() {
        GlobalScope.launch(Dispatchers.Main) {
            twitterDialog.dismiss()
            twitter_login_btn.text =  "Unlink twitter account"
            Log.d("TWITTER-Handler", "Logged in user and linked account")

            //get username here
            refreshConnectedAccounts(true)

        }
    }

    // needed for Facebook
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fHandler.callbackManager.onActivityResult(requestCode, resultCode, data)
        if(fHandler.hasLinkedAccount())
        {
            login_button_facebook.text = getString(R.string.facebook_unlink_text)
        }
        else
        {
            login_button_facebook.text = getString(R.string.facebook_link_text)
        }
        refreshConnectedAccounts(false)
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun loggedOut() {
        login_button_facebook.text = getString(R.string.facebook_link_text)
    }

    /* this method shows the connected accounts as soon as you log in with a social media account
    *  TODO works for now just for twitter, facebook untouched because unclear if facebooks gets removed from app
    *  TODO also works now just for one connected account. */
    private fun refreshConnectedAccounts(twitter: Boolean)
    {
        val sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        val userMail = sharedPref.getString("current_user", "").toString()
        if(twitter || tHandler.hasLinkedAccount())
        {
            rlLayoutTwitter.visibility = View.VISIBLE
            if(userMail == "accountsTest.user@test.com"){
                twitterUserName.text = "Test"
            }
            else{
                twitterUserName.text = tHandler.getTwitterUsername()
            }
        }
        if(fHandler.hasLinkedAccount())
        {
            rlLayoutFacebook.visibility = View.VISIBLE
            if(userMail == "accountsTest.user@test.com"){
                facebookUserName.text = "Test"
            }
            else{
                facebookUserName.text = fHandler.getUser()
            }
        }
        if(rlLayoutFacebook.visibility == View.VISIBLE &&  rlLayoutTwitter.visibility == View.VISIBLE){
            add_account_button.isClickable = false
            add_account_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        }
    }

}