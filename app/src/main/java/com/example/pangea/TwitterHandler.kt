package com.example.pangea

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

class TwitterHandler(private val context: Context, private val user: User) {


    private var twitter: Twitter? = null

    fun initTwitterApi(accToken: twitter4j.auth.AccessToken?) {

        val dbHandler = DatabaseHandler()
        dbHandler.saveTwitterLink(user, accToken?.token, accToken?.tokenSecret, context)

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
        val builder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
                .setOAuthAccessToken(user.twitterAuthToken)
                .setOAuthAccessTokenSecret(user.twitterAuthSecret)
        val config = builder.build()
        val factory = TwitterFactory(config)
        twitter = factory.instance
    }

    fun postTweet(tweet: String): String {
        if(twitter == null) {
            initTwitterApi()
        }

        var statusId = ""
         try {
            val status = twitter?.updateStatus(tweet)
            statusId = (status!!.id).toString()
            "200"
        } catch (e: Exception) {
            Log.e("ERROR: ", e.toString())
            statusId = "-1"
        }

        return statusId
    }

    fun hasLinkedAccount(): Boolean {
        return !(user.twitterAuthToken == null || user.twitterAuthSecret == null)
    }

    fun unlinkAccount() {
        val dbHandler = DatabaseHandler()
        dbHandler.saveTwitterLink(user, null, null, context)
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

    /* Method to get username from connected twitter account
    *  Supports only one twitter account for now */
    fun getTwitterUsername():String
    {
        if(twitter == null) {
            initTwitterApi()
        }
        return twitter?.oAuthAccessToken?.userId.toString()
    }

    /* Method to check if a Twitter user is logged in */
    fun checkIfTwitterObjectValid(): Boolean
    {
        if(twitter == null)
        {
            return false
        }
        return true
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