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

class DashboardsActivity : AppCompatActivity()
{
    lateinit var tabLayout : TabLayout
    lateinit var swipe : ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboards)

        tabLayout = findViewById(R.id.dashboard_bar)
        swipe = findViewById(R.id.ViewPager)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = SwipeAdapter(this, supportFragmentManager,
            tabLayout.tabCount)

        swipe.adapter = adapter
        swipe.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                swipe.currentItem = tab.position

            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }
}
