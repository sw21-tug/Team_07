package com.example.pangea

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import com.facebook.FacebookSdk
import kotlinx.android.synthetic.main.activity_accounts.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@Suppress("DEPRECATION")
class AccountsActivity : AppCompatActivity(), TwitterHandler.ITwitterCallback {
    lateinit var twitterDialog: Dialog
    lateinit var tHandler: TwitterHandler
    lateinit var fHandler: FacebookHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_accounts)
        tHandler = TwitterHandler(this)
        if(tHandler.hasLinkedAccount()) {
            twitter_login_btn.text = "Unlink twitter account"
        }
        else{
            twitter_login_btn.text = "Login with Twitter"
        }

        twitter_login_btn.setOnClickListener {
            if(tHandler.hasLinkedAccount()) {
                tHandler.unlinkAccount()
                twitter_login_btn.text = "Login with Twitter"
            }
            else {
                setupDialogAndRequestAuthToken()
            }
        }

        fHandler = FacebookHandler(this)
        login_button_facebook.setOnClickListener {
            if(fHandler.isLoggedIn())
            {
                fHandler.logoutFacebook();
            }
            else
            {
                fHandler.loginFacebook();
                Toast.makeText(this, "logging in", Toast.LENGTH_LONG).show();
            }
        }
    }

    // needed for Facebook
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fHandler.callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
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
        twitterDialog = Dialog(this)
        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = tHandler.TwitterWebViewClient( this)
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
}