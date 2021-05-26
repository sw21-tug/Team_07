package com.example.pangea

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import java.util.concurrent.TimeUnit

class RegisterAndLoginActivity : AppCompatActivity() {
    private val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_login)

        val userEmail = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val registerButton = findViewById<Button>(R.id.startRegisterButton);

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

        val periodWork = PeriodicWorkRequest.Builder(NotificationWorker::class.java,1,TimeUnit.DAYS)
            .addTag("periodic-pending-notification")
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("periodic-pending-notification", ExistingPeriodicWorkPolicy.REPLACE, periodWork)


    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}

