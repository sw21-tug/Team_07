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
import twitter4j.StatusUpdate
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import java.io.File
import java.net.URI

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

    fun postTweet(tweet: String, image_path: String): String {
        if(twitter == null) {
            initTwitterApi()
        }
        var statusId = ""

        if(image_path.isNullOrEmpty())
        {

            try {
                val status = twitter?.updateStatus(tweet)
                statusId = (status!!.id).toString()
                "200"

            } catch (e: Exception) {
                Log.e("ERROR: ", e.toString())
                statusId = "-1"
            }
        }
        else
        {
            val imgFile: File = File(image_path)

            try {

                val status = StatusUpdate(tweet)

                status.setMedia(imgFile)

                val finished_post = twitter?.updateStatus(status)
                statusId = (finished_post!!.id).toString()


            } catch (e: Exception) {
                Log.e("ERROR: ", e.toString())
                statusId = "-1"
            }
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
    
    fun getFavorites(postId: String) : String {
        if(postId == "" || user.twitterAuthToken == null || user.twitterAuthSecret == null)
        {
            return "0"
        }

        if(twitter == null) {
            initTwitterApi()
        }

        var retweets = twitter?.getRetweets(postId.toLong())
        var number_of_retweets = 0
        if(!retweets.isNullOrEmpty())
        {
            number_of_retweets = retweets.get(0).retweetCount
        }
       
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