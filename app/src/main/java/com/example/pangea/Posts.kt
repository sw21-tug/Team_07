package com.example.pangea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

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
        return inflater.inflate(R.layout.posts_view, container, false)
    }
}