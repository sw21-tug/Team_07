package com.example.pangea

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_dashboards.*

/* This class controls the overall dashboard view
   and the switching between the different Tabs
   Layout-File: activity_dashboards.xml */
class DashboardsActivity : BaseActivity()
{
    lateinit var tabLayout : TabLayout
    lateinit var swipe : ViewPager
    lateinit var accountsTab : Accounts

    //creates the view
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboards)

        //setSupportActionBar(findViewById(R.id.toolbar))

        tabLayout = findViewById(R.id.dashboard_bar)
        swipe = findViewById(R.id.ViewPager)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        accountsTab = Accounts()
        val adapter = SwipeAdapter(this, supportFragmentManager,
                tabLayout.tabCount, accountsTab)


        swipe.adapter = adapter
        swipe.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setupWithViewPager(swipe)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                swipe.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        findViewById<Toolbar>(R.id.toolbar).setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.action_settings -> {
                    val settings = Intent(this, SettingsActivity::class.java)
                    startActivity(settings)
                    // User chose the "Settings" item, show the app settings UI...
                    true
                }

                R.id.action_logout -> {
                    val sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("current_user", null).apply()
                    finish()
                    // User chose the "Favorite" action, mark the current item
                    // as a favorite...
                    true
                }

                else -> {
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    super.onOptionsItemSelected(item)
                }
            }
        }
    }

    // needed for Facebook
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        accountsTab.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
