package com.example.pangea

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
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

    fun getFavorites(postId: String) : String {
        if(postId == "" || user.twitterAuthToken == null || user.twitterAuthSecret == null)
        {
            return "0"
        }

        if(twitter == null) {
            initTwitterApi()
        }

        var retweets = twitter?.getRetweets(postId.toLong())

        var number_of_retweets = retweets?.size

        return number_of_retweets.toString()
    }

    inner class TwitterWebViewClient(private val caller: ITwitterCallback) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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