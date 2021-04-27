package com.example.pangea


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider
import kotlinx.android.synthetic.*


/* This class controls the logic in the "Posts"-Tab
   New Methods can be implemented as needed.
   Layout-File: post_view.xml */
class Posts : Fragment()
{
    //creates the view (post_view.xml)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // get all posts for user in db "test.user@test.com"
        // show them in posts tab

        val email = "test.user@test.com"
        val register = com.example.pangea.DatabaseHandler()
        val posts = activity?.let { register.getAllPosts(email, it.applicationContext) }


        val view = inflater.inflate(R.layout.posts_view, container, false)
        val linearLayout : LinearLayout = view.findViewById(R.id.linearLayout)
        val textView = TextView(activity?.applicationContext)
        textView.text = email
        val textid = 10
        textView.id = textid
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        textView.layoutParams = params
        textView.textSize = 24F
        textView.setTextColor(Color.RED)
        linearLayout.addView(textView)

        return view
    }

    fun fetchPosts() {}
}