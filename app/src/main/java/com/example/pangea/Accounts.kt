package com.example.pangea

import android.app.Activity
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.account_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/* This class controls the logic in the "Accounts"-Tab
   New Methods can be implemented as needed.
   Layout-File: account_view.xml */
class Accounts(private var user: User) : Fragment(), TwitterHandler.ITwitterCallback
{
    //creates the view (account_view.xml)
    lateinit var twitterDialog: Dialog
    lateinit var tHandler: TwitterHandler
    lateinit var twitter_login_btn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.account_view, container, false)
        twitter_login_btn = view.findViewById(R.id.twitter_login_btn)
        val context = activity as AppCompatActivity
        tHandler = TwitterHandler(context, user)
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
        twitterDialog = Dialog(activity?.applicationContext!!)
        val webView = WebView(activity?.applicationContext!!)
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