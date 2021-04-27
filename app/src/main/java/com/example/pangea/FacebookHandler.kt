package com.example.pangea

import android.content.Context
import android.util.Log
import android.widget.Button
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


class FacebookHandler(private val context: Context)
{
    var callbackManager: CallbackManager = CallbackManager.Factory.create();

    fun isLoggedIn(): Boolean
    {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    fun hasLinkedAccount(): Boolean
    {
        val sharedPref = context.getSharedPreferences("facebook", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("facebook_oauth_token", "")
        return accessToken != ""
    }

    // onclickListener
    fun loginFacebook()
    {
        Log.d("TAG", "LoginFacebook")

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>
        {
                override fun onSuccess(result: LoginResult)
                {
                    Log.d("TAG", "Success Login")
                    val access_token = result.accessToken;
                    AccessToken.setCurrentAccessToken(access_token); // set Current accessToken
                    val sharedPref = context.getSharedPreferences("facebook", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("facebook_oauth_token", access_token?.token ?: "").apply()
                }

                override fun onCancel()
                {
                    // user closes login popup, safe to ignore
                    Log.d("TAG", "Cancel Login");
                }

                override fun onError(error: FacebookException?)
                {
                    Log.d("TAG", "Error with Facebook Login: " + error.toString());
                    //Toast.makeText(context, "error: "+error.toString(), Toast.LENGTH_LONG).show();
                    TODO("Not yet implemented")
                }
        });
    }

    fun logoutFacebook()
    {
        val sharedPref = context.getSharedPreferences("facebook", Context.MODE_PRIVATE)
        sharedPref.edit().putString("facebook_oauth_token", "").apply()
        if(AccessToken.getCurrentAccessToken() != null)
        {
            GraphRequest(AccessToken.getCurrentAccessToken(),
                "/me/permissions/", null, HttpMethod.DELETE,
                GraphRequest.Callback {
                    AccessToken.setCurrentAccessToken(null);
                    LoginManager.getInstance().logOut()
                })
        }
    }
}