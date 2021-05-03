package com.example.pangea

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    lateinit var userEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userEmail =  intent.getStringExtra("loggedInUserMail").toString()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment(userEmail))
                    .commit()
        }
    }



    class SettingsFragment(var userEmail: String) : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceManager.preferenceDataStore = null

            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val editTextPreference: EditTextPreference? = findPreference("password")

            editTextPreference?.setOnBindEditTextListener {editText ->
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                editText.text.clear()
            }

            editTextPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                updatePassword(newValue as String)
                true
            }

        }

        private fun updatePassword(pwd: String) {
            GlobalScope.launch(Dispatchers.IO) {
                var db = DatabaseHandler()
                db.changePassword(userEmail, pwd, requireContext())
            }

        }
    }


}