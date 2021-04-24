package com.example.pangea

import android.content.Context
import android.widget.Button
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder


class FacebookHandler(private val context: Context)
{
    private var facebook: FacebookSdk? = null;

    fun getAccessToken(): AccessToken
    {
        return AccessToken.getCurrentAccessToken();
    }

    fun isLoggedIn(access_token: AccessToken): Boolean
    {
        return !access_token.isExpired;
    }

    // onclickListener
    fun loginFacebook(facebook_login_button: LoginButton)
    {
        val callbackManager = CallbackManager.Factory.create();
        facebook_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult>
        {
                override fun onSuccess(result: LoginResult)
                {
                    // access_token
                    val access_token = result.accessToken;
                    AccessToken.setCurrentAccessToken(access_token); // set Current accessToken
                    TODO("Not yet implemented")
                }

                override fun onCancel()
                {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException?)
                {
                    TODO("Not yet implemented")
                }

            });
        //manageLogin();
    }

    fun logoutFacebook(facebook_logout_btn: Button)
    {
        facebook_logout_btn.setOnClickListener {
            if(AccessToken.getCurrentAccessToken() != null)
            {
                GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/permissions/", null, HttpMethod.DELETE,
                    GraphRequest.Callback {
                        AccessToken.setCurrentAccessToken(null);
                        LoginManager.getInstance().logOut();
                        finish();
                    })
            }
        }
    }

    /*fun manageLogin()
    {
        val callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    // manage login
                    TODO("Not yet implemented")
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException?) {
                    TODO("Not yet implemented")
                }

                val access_token = AccessToken.getCurrentAccessToken();

            });

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

    }*/


}