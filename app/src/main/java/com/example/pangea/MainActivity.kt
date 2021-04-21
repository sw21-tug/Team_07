package com.example.pangea

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val callbackManager = CallbackManager.Factory.create();

        // get LoginButton
        val loginButton = findViewById<LoginButton>(R.id.login_button);

        loginButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Loging in!", Toast.LENGTH_LONG).show();

        }
        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {

                        Toast.makeText(this@MainActivity, "Loging in!", Toast.LENGTH_LONG).show();
                        TODO("Not yet implemented")
                    }

                    override fun onCancel() {
                        TODO("Not yet implemented")
                    }

                    override fun onError(error: FacebookException?) {
                        TODO("Not yet implemented")
                    }

        });



    }
}