package com.example.pangea

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class RegisterAndLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        var prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var darkMode = prefs.getBoolean("theme", false);
        if(darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        delegate.applyDayNight()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_login)

        val userEmail = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val registerButton = findViewById<Button>(R.id.registerButton);

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.loginButton);
        loginButton.setOnClickListener {
            loginButton.hideKeyboard()
            val register = DatabaseHandler()
            val user = register.getRegisteredUser(userEmail.text.toString(), applicationContext)
            if (user != null && user.password.equals(password.text.toString())) {
                val sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE)
                sharedPref.edit().putString("current_user", user.email).apply()
                val intent = Intent(this, DashboardsActivity::class.java)
                startActivity(intent)
                userEmail.text.clear()
                password.text.clear()
            } else
            {
                val myToast = Toast.makeText(applicationContext,"Login failed" ,Toast.LENGTH_SHORT)
                myToast.show()
            }
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}

