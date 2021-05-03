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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class RegisterActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val userEmail = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val registerButton = findViewById<Button>(R.id.registerButton);

        registerButton.setOnClickListener {
            registerButton.hideKeyboard()
            val register = DatabaseHandler()
            register.registerUser(userEmail.toString(), password.toString(), applicationContext)
            val myToastSuccess = Toast.makeText(applicationContext,"Registration successful" ,Toast.LENGTH_SHORT)
            myToastSuccess.show()
            val user = register.getRegisteredUser(userEmail.toString(), applicationContext)
            val intent = Intent(this, DashboardsActivity::class.java)
            intent.putExtra("loggedInUserMail", user.email)
            startActivity(intent)
        }
    }
    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}