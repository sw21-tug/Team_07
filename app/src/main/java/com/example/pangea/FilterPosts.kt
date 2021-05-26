package com.example.pangea

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_filter_posts.*
import kotlinx.android.synthetic.main.posts_popup.*

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

            }
            else if (toggleByDate.isChecked){

            }
            else if(toggleByPlatform.isChecked) {

            }

        }
    }
}