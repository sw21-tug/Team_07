package com.example.pangea

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val password = findViewById<EditText>(R.id.newPassword)
        val sharedPreference = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userEmail = sharedPreference.getString("useremail", " ")
        val changePassword = findViewById<Button>(R.id.changePassword)
        changePassword.setOnClickListener()
        {
            val db = com.example.pangea.DatabaseHandler()
            db.changePassword(userEmail.toString(), password.toString(), applicationContext)
            val myToast = Toast.makeText(applicationContext, "Password changed", Toast.LENGTH_SHORT)
            myToast.show()
            changePassword.hideKeyboard()
        }

    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}