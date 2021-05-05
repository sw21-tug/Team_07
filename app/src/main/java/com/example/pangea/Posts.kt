package com.example.pangea


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.posts_popup.*
import kotlinx.android.synthetic.main.posts_view.*
import kotlinx.android.synthetic.main.posts_view.view.*


/* This class controls the logic in the "Posts"-Tab
   New Methods can be implemented as needed.
   Layout-File: post_view.xml */
class Posts(private val register_intent: Intent) : Fragment()
{
    //creates the view (post_view.xml)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val view: View = inflater.inflate(R.layout.posts_view, container, false)


        val userEmail = register_intent.getStringExtra("loggedInUserMail").toString()
        val register = DatabaseHandler()
        val user = activity?.applicationContext?.let { register.getRegisteredUser(userEmail, it) }

        view.sendpostbtn.setOnClickListener {
            val intent = Intent(context, PostsPopup::class.java)
            if (user != null) {
                intent.putExtra("loggedInUserMail", user.email)
            }
            startActivity(intent)
        }

        return view
    }


    fun postMessage()
    {
    }

}