package com.example.pangea

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

class TwitterHandler(private val context: Context) {

    private var twitter: Twitter? = null

    fun initTwitterApi(accToken: twitter4j.auth.AccessToken?) {

        val sharedPref = context.getSharedPreferences("twitter", Context.MODE_PRIVATE)
        sharedPref.edit().putString("twitter_oauth_token", accToken?.token ?: "").apply()
        sharedPref.edit().putString("twitter_oauth_token_secret", accToken?.tokenSecret ?: "").apply()
        val builder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
            .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
            .setOAuthAccessToken(accToken?.token)
            .setOAuthAccessTokenSecret(accToken?.tokenSecret)
        val config = builder.build()
        val factory = TwitterFactory(config)
        twitter = factory.instance
    }

    fun initTwitterApi() {
        val sharedPref = context.getSharedPreferences("twitter", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("twitter_oauth_token", "")
        val accessTokenSecret = sharedPref.getString("twitter_oauth_token_secret", "")
        val builder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret)
        val config = builder.build()
        val factory = TwitterFactory(config)
        twitter = factory.instance
    }

    fun postTweet(tweet:String): String {
        if(twitter == null) {
            initTwitterApi()
        }

        return try {
            val status = twitter?.updateStatus(tweet)
            "200"
        } catch (e: Exception) {
            Log.e("ERROR: ", e.toString())
            "-1"
        }
    }

    fun hasLinkedAccount(): Boolean {
        val sharedPref = context.getSharedPreferences("twitter", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("twitter_oauth_token", "")
        val accessTokenSecret = sharedPref.getString("twitter_oauth_token_secret", "")

        return !(accessToken == "" || accessTokenSecret == "")
    }

    fun unlinkAccount() {
        val sharedPref = context.getSharedPreferences("twitter", Context.MODE_PRIVATE)
        sharedPref.edit().putString("twitter_oauth_token", "").apply()
        sharedPref.edit().putString("twitter_oauth_token_secret", "").apply()
        twitter = null
    }

    fun getTwitterConnector():Twitter?  {
        val builder = ConfigurationBuilder()
            .setDebugEnabled(true)
            .setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
            .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
        val config = builder.build()
        val factory = TwitterFactory(config)
        twitter = factory.instance

        return twitter
    }

    inner class TwitterWebViewClient(private val caller: ITwitterCallback) : WebViewClient() {

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            val url = request!!.url.toString()
            if (url.contains(TwitterConstants.CALLBACK_URL)) {
                handleUrl(url)
                caller.oAuthResponse()
            }
            return super.shouldInterceptRequest(view, request)
        }

        // Get the oauth_verifier
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
            GlobalScope.launch(Dispatchers.IO) {
                val accToken = twitter?.getOAuthAccessToken(oauthVerifier)
                initTwitterApi(accToken)
            }
        }
    }

    interface ITwitterCallback {

        fun oAuthResponse()
    }
}