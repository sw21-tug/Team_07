package com.example.pangea

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.util.*

open class BaseActivity : AppCompatActivity() {

    private var locale: Locale = Locale.getDefault()

    override fun onStart() {
        locale = getLocale(this)
        super.onStart()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextUtils(newBase).updateLocale(newBase, getLocale(newBase)))
    }

    override fun onRestart() {

        if(getLocale(this) != locale)
            recreate()
        super.onRestart()
    }

    private fun getLocale(context: Context?): Locale {
        val sharedPref = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val userEmail = sharedPref?.getString("current_user", "").toString()
        var lang = DatabaseHandler().getRegisteredUser(userEmail, SettingsActivity()).language.toString()
        return Locale(lang)
    }
}