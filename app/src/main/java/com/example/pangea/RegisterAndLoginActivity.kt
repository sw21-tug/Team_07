package com.example.pangea

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterAndLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_login)

        val userEmail = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val registerButton = findViewById<Button>(R.id.registerButton);

        registerButton.setOnClickListener {
            registerButton.hideKeyboard()
            val register = com.example.pangea.DatabaseHandler()
            register.registerUser(userEmail.toString(), password.toString(), applicationContext)

            val myToast = Toast.makeText(applicationContext,"Registration successful" ,Toast.LENGTH_LONG)
            myToast.show()
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

