package com.example.pangea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/* This class controls the logic in the "Bookmarked"-Tab
   New Methods can be implemented as needed.
   Layout-File: bookmarked_view.xml */
class Bookmarked : Fragment()
{
    //creates the view (bookmarked_view.xml)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bookmarked_view, container, false)
    }
}