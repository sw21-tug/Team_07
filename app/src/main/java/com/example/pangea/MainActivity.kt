package com.example.pangea

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*ATTENTION - START
        * This starts the activity dashboard, if you don't need a button after logging in
        * just delete the Button and use the intent and startActivity() like shown below. */
        val accounts: Button = findViewById(R.id.dashboard)
        accounts.setOnClickListener {
            val intent = Intent(this@MainActivity, DashboardsActivity::class.java)
            startActivity(intent)
        }
        /*ATTENTION - END*/
    }
}

