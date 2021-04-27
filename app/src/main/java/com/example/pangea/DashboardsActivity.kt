package com.example.pangea

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

/* This class controls the overall dashboard view
   and the switching between the different Tabs
   Layout-File: activity_dashboards.xml */
class DashboardsActivity : AppCompatActivity()
{
    lateinit var tabLayout : TabLayout
    lateinit var swipe : ViewPager
    lateinit var accountsTab : Accounts

    //creates the view
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboards)


        val userEmail = intent.getStringExtra("loggedInUserMail").toString()
        val user = DatabaseHandler().getRegisteredUser(userEmail, this)

        tabLayout = findViewById(R.id.dashboard_bar)
        swipe = findViewById(R.id.ViewPager)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        accountsTab = Accounts(user)
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

    }

    // needed for Facebook
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        accountsTab.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
