package com.example.pangea

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/* This class controls the logic in the "Accounts"-Tab
   New Methods can be implemented as needed.
   Layout-File: account_view.xml */
class Accounts : Fragment()
{
    //creates the view (account_view.xml)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_view, container, false)
    }


}