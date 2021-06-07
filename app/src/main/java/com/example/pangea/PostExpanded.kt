package com.example.pangea

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.posts_expanded.*
import kotlinx.android.synthetic.main.posts_popup.*
import kotlinx.android.synthetic.main.posts_view.*

class PostExpanded : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.posts_expanded)

        if (Build.VERSION.SDK_INT > 9) {
            val policy: StrictMode.ThreadPolicy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        TextViewPostExpanded.text = getIntent().getStringExtra("Text")
        FacebookLikes.text = getIntent().getStringExtra("FBReactions")
        TwitterLikes.text = getIntent().getStringExtra("TwitterRetweets")
        ImagePostExpanded.setImageURI(Uri.parse(getIntent().getStringExtra("Image")))

        var facebook = getIntent().getStringExtra("facebook")!!.toBoolean()
        var twitter = getIntent().getStringExtra("twitter")!!.toBoolean()

        if(!facebook)
        {
            FacebookLikes.visibility = View.INVISIBLE
            fblikestext.visibility = View.INVISIBLE
        }
        if(!twitter)
        {
            TwitterLikes.visibility = View.INVISIBLE
            twlikestext.visibility = View.INVISIBLE
        }

    }
}