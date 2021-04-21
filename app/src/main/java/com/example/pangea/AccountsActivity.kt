package com.example.pangea

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_accounts.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountsActivity : AppCompatActivity(), TwitterHandler.ITwitterCallback {

    lateinit var twitterDialog: Dialog
    lateinit var tHandler: TwitterHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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