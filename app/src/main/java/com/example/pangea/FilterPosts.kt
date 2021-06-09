package com.example.pangea

import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import kotlinx.android.synthetic.main.activity_filter_posts.*
import kotlinx.android.synthetic.main.posts_popup.*
import java.text.SimpleDateFormat
import java.util.*

class FilterPosts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_posts)


        toggleByContent.setOnCheckedChangeListener { compoundButton, b ->

            if(b){
                toggleByDate.isChecked = false
                toggleByPlatform.isChecked = false
                filter_post_content.visibility = View.VISIBLE

            }
            else{
                filter_post_content.visibility = View.GONE
                filter_post_content.text.clear()
            }
        }

        toggleByDate.setOnCheckedChangeListener { compoundButton, b ->

            if(b){
                toggleByContent.isChecked = false
                toggleByPlatform.isChecked = false
                dpFilter.visibility = View.VISIBLE
            }
            else{
                dpFilter.visibility = View.GONE
            }
        }

        toggleByPlatform.setOnCheckedChangeListener { compoundButton, b ->

            if(b){
                toggleByDate.isChecked = false
                toggleByContent.isChecked = false

                rb_filter_facebook.visibility = View.VISIBLE
                rb_filter_twitter.visibility = View.VISIBLE
                fb_icon.visibility = View.VISIBLE
                twitter_icon.visibility = View.VISIBLE
            }
            else{
                rb_filter_facebook.visibility = View.GONE
                rb_filter_twitter.visibility = View.GONE
                fb_icon.visibility = View.GONE
                twitter_icon.visibility = View.GONE
            }
        }

        rb_filter_twitter.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                rb_filter_facebook.isChecked = false
            }
        }

        rb_filter_facebook.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                rb_filter_twitter.isChecked = false
            }
        }

        btn_set_filter.setOnClickListener {

            if(toggleByContent.isChecked){
                GlobalVariable.Companion.matchedPosts = emptyList()
                val userEmail = intent.getStringExtra("loggedInUserMail").toString()
                val register = DatabaseHandler()
                val postsLists = register.getAllPosts(userEmail, this)
                postsLists.forEach{
                    if(it.message.toString().contains(filter_post_content.text.toString())){
                        GlobalVariable.Companion.matchedPosts += it
                    }
                }

                if (GlobalVariable.Companion.matchedPosts.isEmpty())
                {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("INFO")
                    builder.setMessage("No matching posts found.")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    builder.setNeutralButton("Ok"){dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                else
                {
                    finish()
                }
            }

            else if (toggleByDate.isChecked){
                GlobalVariable.Companion.matchedPosts = emptyList()
                val calender = Calendar.getInstance()
                calender.set(dpFilter.year, dpFilter.month, dpFilter.dayOfMonth)
                val sdate = SimpleDateFormat("dd-MM-yyyy")
                val date = sdate.format(calender.time)
                val userEmail = intent.getStringExtra("loggedInUserMail").toString()
                val register = DatabaseHandler()
                val postsLists = register.getAllPosts(userEmail, this)
                postsLists.forEach{
                    if(it.date.toString().equals(date)){
                        GlobalVariable.Companion.matchedPosts += it
                    }
                }
                if (GlobalVariable.Companion.matchedPosts.isEmpty())
                {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("INFO")
                    builder.setMessage("No matching posts found.")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    builder.setNeutralButton("Ok"){dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                else
                {
                    finish()
                }
            }

            else if(toggleByPlatform.isChecked) {
                GlobalVariable.Companion.matchedPosts = emptyList()
                val userEmail = intent.getStringExtra("loggedInUserMail").toString()
                val register = DatabaseHandler()
                val postsLists = register.getAllPosts(userEmail, this)
                postsLists.forEach {
                    if ((rb_filter_facebook.isChecked && it.facebook.equals(true)) || (rb_filter_twitter.isChecked && it.twitter!!.equals(true))) {
                        GlobalVariable.Companion.matchedPosts += it
                    }
                }
                if (GlobalVariable.Companion.matchedPosts.isEmpty())
                {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("INFO")
                    builder.setMessage("No matching posts found.")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    builder.setNeutralButton("Ok"){dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                else
                {
                    finish()
                }
            }

            else {
                GlobalVariable.Companion.matchedPosts = emptyList()
                finish()
            }

        }
    }
}