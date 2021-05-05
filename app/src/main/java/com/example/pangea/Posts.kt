package com.example.pangea

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment


/* This class controls the logic in the "Posts"-Tab
   New Methods can be implemented as needed.
   Layout-File: post_view.xml */
class Posts() : Fragment()
{
    //creates the view (post_view.xml)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = sharedPref.getString("current_user", "").toString()
        val register = DatabaseHandler()
        var posts = emptyList<Post>()

        if(!email.isNullOrEmpty())
        {
            posts = activity?.let { register.getAllPosts(email, it.applicationContext) }!!
        }

        val view = inflater.inflate(R.layout.posts_view, container, false)
        val linearLayout : LinearLayout = view.findViewById(R.id.linearLayoutPosts)

        for (post in posts) {
            var imageParams: RelativeLayout.LayoutParams
            imageParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            val img = ImageView(context)
            img.layoutParams = imageParams

            if(post.facebook) {
                img.setImageResource(R.drawable.facebookiconpreview)
            }
            else {
                img.setImageResource(R.drawable.twitter_bird_logo_2012_svg)
            }
            linearLayout.addView(img)


            val cardView = activity?.applicationContext?.let { CardView(it) }
            if (cardView != null) {
                cardView.minimumWidth = 300
                cardView.minimumHeight = 100
                cardView.setContentPadding(15, 0, 15, 15)
                cardView.setCardBackgroundColor(Color.LTGRAY)
                cardView.radius = 20f
            }

            val textView = TextView(activity?.applicationContext)
            textView.text = post.message

            textView.textSize = 18F
            textView.setTextColor(Color.DKGRAY)
            cardView?.addView(textView)
            linearLayout.addView(cardView)
        }

        return view
    }
}