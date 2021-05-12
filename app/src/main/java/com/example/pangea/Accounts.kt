package com.example.pangea

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.facebook.FacebookSdk
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    lateinit var hidden_facebook_button: Button
    lateinit var add_account_button: FloatingActionButton
    lateinit var twitter_image: ImageView


    /* delete if we don't need facebook - START */
    lateinit var facebook_image: ImageView
    /* delete if we don't need facebook - END */

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(context);
        val view = inflater.inflate(R.layout.account_view, container, false)
        val sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        val userMail = sharedPref.getString("current_user", "").toString()

        val user = DatabaseHandler().getRegisteredUser(userMail, requireContext())

        twitter_login_btn = view.findViewById(R.id.twitter_login_btn)
        login_button_facebook = view.findViewById(R.id.login_button_facebook)
        hidden_facebook_button = view.findViewById(R.id.hidden_facebook_button)

        add_account_button = view.findViewById(R.id.addaccount)

        facebook_image = view.findViewById(R.id.facebook_img2)
        twitter_image = view.findViewById(R.id.imageView2)

        add_account_button.setOnClickListener {



            twitter_image.visibility = View.VISIBLE
            twitter_image.isClickable = true

            /* delete if we don't need facebook - START */

            facebook_image.visibility = View.VISIBLE
            facebook_image.isClickable = true
            /* delete if we don't need facebook - END */


        }

        twitter_image.setOnClickListener {
            twitter_login_btn.isClickable = true
            twitter_login_btn.performClick()
            twitter_login_btn.isClickable = false

            twitter_image.visibility = View.INVISIBLE
            facebook_image.visibility = View.INVISIBLE

        }

        facebook_image.setOnClickListener {
            login_button_facebook.isClickable = true
            login_button_facebook.performClick()
            login_button_facebook.isClickable = false

            twitter_image.visibility = View.INVISIBLE
            facebook_image.visibility = View.INVISIBLE
        }


        //twitter_image.visibility = View.INVISIBLE
        /* old copy start here  */


        val context = activity as AppCompatActivity
        tHandler = TwitterHandler(context, user)
        if(tHandler.hasLinkedAccount()) {
            twitter_login_btn.text = getString(R.string.twitter_unlink_text)
        }
        else{
            twitter_login_btn.text = getString(R.string.twitter_link_text)
        }

        twitter_login_btn.setOnClickListener {
            if(tHandler.hasLinkedAccount()) {
                tHandler.unlinkAccount()
                twitter_login_btn.text =getString(R.string.twitter_link_text)
            }
            else {
                setupDialogAndRequestAuthToken()
            }
        }

        fHandler = FacebookHandler(context, user, activity)
        fHandler.initApi(this)
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
            if(fHandler.isLoggedIn())
            {
                fHandler.logoutFacebook();
            }
            else
            {
                fHandler.loginFacebook();
            }
        }


        return view
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
        super.onActivityResult(requestCode, resultCode, data)


    }

    override fun loggedOut() {
        login_button_facebook.text = getString(R.string.facebook_link_text)
    }

}