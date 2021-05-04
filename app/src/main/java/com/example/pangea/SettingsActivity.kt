package com.example.pangea

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SettingsActivity : BaseActivity() {

    lateinit var userEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActionBar()?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.settings_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userEmail =  intent.getStringExtra("loggedInUserMail").toString()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment() : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceManager.preferenceDataStore = null

            val sharedPref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            val userMail = sharedPref.getString("current_user", "").toString()

            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val pwdPref: EditTextPreference? = findPreference("password")

            pwdPref?.setOnBindEditTextListener {editText ->
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                editText.text.clear()
            }

            pwdPref?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                GlobalScope.launch(Dispatchers.IO) {
                    var db = DatabaseHandler()
                    db.changePassword(userMail, newValue.toString(), requireContext())
                }
                true
            }

            val langPref: DropDownPreference? = findPreference("language")

            langPref?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                GlobalScope.launch(Dispatchers.IO) {
                    var db = DatabaseHandler()
                    db.updateUserLanguage(userMail, newValue.toString(), requireContext())
                }
                GlobalScope.launch(Dispatchers.Main){
                    requireActivity().recreate()
                }

                true
            }

        }

    }


}