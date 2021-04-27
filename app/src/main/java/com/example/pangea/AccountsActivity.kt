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
class AccountsActivity : AppCompatActivity() {

    lateinit var fHandler: FacebookHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_accounts)

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
}