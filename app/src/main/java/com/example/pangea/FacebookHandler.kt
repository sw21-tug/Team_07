package com.example.pangea

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


class FacebookHandler(private val context: Context, private val user: User, private var activity1: FragmentActivity?)
{
    lateinit var caller: IFacebookCallback
    lateinit var acces_token_string: String
    lateinit var id: String
    lateinit var facebookPostId: String
    lateinit var facebookReactions: String


    var callbackManager: CallbackManager = CallbackManager.Factory.create();
    var accessTokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
        ) {
            if (currentAccessToken == null) {
                caller.loggedOut()
            }
        }
    }

    fun  initApi(caller: IFacebookCallback){
        this.caller = caller
        accessTokenTracker.startTracking()
    }

    fun getCurrentAccesToken(): AccessToken
    {
        return AccessToken.getCurrentAccessToken()
    }


    fun isLoggedIn(): Boolean
    {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    fun getUser() : String
    {
        return getCurrentAccesToken().userId
    }
    fun isLoggedInWithWritePermissions(): Boolean
    {
        if(!isLoggedIn()) {
            loginFacebook()
        }
        val permission = AccessToken.getCurrentAccessToken().permissions
        return permission == arrayOf("publish_actions")
    }

    fun hasLinkedAccount(): Boolean
    {
        return !(user.facebookAuthToken == null)
    }

    fun loginFacebook()
    {
        Log.d("TAG", "LoginFacebook")

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>
        {
                override fun onSuccess(result: LoginResult)
                {
                    Log.d("TAG", "Success Login")
                    val access_token = result.accessToken;
                    val dbHandler = DatabaseHandler()
                    dbHandler.saveFacebookLink(user, access_token?.token, context)
                    AccessToken.setCurrentAccessToken(access_token); // set Current accessToken
                }

            override fun onCancel() {
                // user closes login popup, safe to ignore
                Log.d("TAG", "Cancel Login");
            }

            override fun onError(error: FacebookException?) {
                Log.d("TAG", "Error with Facebook Login: " + error.toString());
                //Toast.makeText(context, "error: "+error.toString(), Toast.LENGTH_LONG).show();
                TODO("Not yet implemented")
            }
        });
    }

    fun logoutFacebook()
    {
        val dbHandler = DatabaseHandler()
        dbHandler.saveFacebookLink(user, null, context)
        LoginManager.getInstance().logOut()
    }

    interface IFacebookCallback {

        fun loggedOut()
    }

    fun postMessage(msg: String, image_path: String) : Any
    {
        if(!isLoggedIn()) {
            loginFacebook()
        }

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/accounts",
                null,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    acces_token_string = (((response.jsonObject["data"] as JSONArray)[0]) as JSONObject).get("access_token").toString()

                    id = (((response.jsonObject["data"] as JSONArray)[0]) as JSONObject).get("id").toString()
                }
        ).executeAndWait()

        val permissions: Collection<String>? = mutableListOf("pages_read_engagement", "pages_manage_engagement")
        val page_acces = AccessToken(acces_token_string, "171191854853298", getCurrentAccesToken().userId, permissions, null, null, null, null, null, null, null)
        val page_id ="/"+id +"/feed"


        val jsonobj: JSONObject = JSONObject()
        jsonobj.put("message", msg)


        if(image_path.isNullOrBlank())
        {
        val request = GraphRequest.newPostRequest(
                page_acces,
                page_id,
                jsonobj)
        { response ->
            Toast.makeText(context, "success!", Toast.LENGTH_LONG).show()
            facebookPostId = response.jsonObject.get("id").toString()
            }
            request.executeAndWait()
        }
        else
        {
            val page_photo_id = "/"+id +"/photos"
            val file_to_upload = File(image_path)
            val request = GraphRequest.newUploadPhotoRequest(
                page_acces,
                page_photo_id,
                file_to_upload,
                msg,
                null)
            {   response ->
                Toast.makeText(context, "success!", Toast.LENGTH_LONG).show()
                facebookPostId = response.jsonObject.get("post_id").toString()

            }
            request.executeAndWait()
        }

        return facebookPostId
    }

    fun getReactions(postId: String?) : String {
        if(postId == "" || postId == null || !isLoggedIn())
        {
            return "0"
        }

        val request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + postId
        ) {
            response ->
            facebookReactions = ((response.jsonObject.get("reactions") as JSONObject).get("summary") as JSONObject).get("total_count").toString()
        }

        val parameters = Bundle()
        parameters.putString("fields", "reactions.summary(total_count)")
        request.parameters = parameters
        request.executeAndWait()

        return facebookReactions
    }
}