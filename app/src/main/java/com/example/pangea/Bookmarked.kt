package com.example.pangea

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.bookmarked_view.view.*

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
        val sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = sharedPref.getString("current_user", "").toString()
        val register = DatabaseHandler()
        var posts = emptyList<Post>()
        val view = inflater.inflate(R.layout.bookmarked_view, container, false)

        if (Build.VERSION.SDK_INT > 9) {
            val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        view.bookmarkedRefreshButton.setOnClickListener{
            if(!email.isNullOrEmpty())
            {
                posts = activity?.let { register.getAllBookmarkedPosts(email, it.applicationContext) }!!
            }

            val linearLayout : LinearLayout = view.findViewById(R.id.linearLayoutBookmarked)
            linearLayout.removeAllViews();

            for (post in posts) {

                val layoutInflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val new_view = layoutInflater!!.inflate(R.layout.single_post, null)

                val cardview = new_view.findViewById<ConstraintLayout>(R.id.post_card_view)
                val textfield = new_view.findViewById<TextView>(R.id.post_text_field)
                val bookmark_checkbox = new_view.findViewById<CheckBox>(R.id.bookmark_checkbox)
                val posted_to_fb = new_view.findViewById<ImageView>(R.id.posted_to_fb)
                val posted_to_tw = new_view.findViewById<ImageView>(R.id.posted_to_tw)
                val post_date = new_view.findViewById<TextView>(R.id.post_date)
                bookmark_checkbox.visibility = View.INVISIBLE

                textfield.text = post.message

                if(post.facebook)
                {
                    posted_to_fb.alpha = 1.0F
                }
                if(post.twitter == true)
                {
                    posted_to_tw.alpha = 1.0F
                }

                post_date.text = post.date

                textfield.setOnClickListener {
                    val intent = Intent(context, PostExpanded::class.java)
                    intent.putExtra("Text", post.message)
                    intent.putExtra("Image", post.image)
                    val curr_user: User = register.getRegisteredUser(email, requireContext())
                    val fhandler = FacebookHandler(requireContext(), curr_user, activity)

                    val thandler = TwitterHandler(requireContext(), curr_user)

                    var twretweets = "0"
                    var fblikes = "0"

                    if(post.twitter == true) {
                        twretweets = thandler.getFavorites(post.postID!!)
                    }
                    if(post.facebook) {
                        fblikes = fhandler.getReactions(post.postID)
                    }


                    intent.putExtra("TwitterRetweets", twretweets)
                    intent.putExtra("FBReactions", fblikes)

                    intent.putExtra("twitter", post.twitter.toString())
                    intent.putExtra("facebook", post.facebook.toString())

                    startActivity(intent)
                }

                textfield.setOnLongClickListener {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Delete Post")
                    builder.setMessage("Do you want to delete this post?")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    builder.setPositiveButton("Yes"){dialogInterface, which ->
                        register.deletePostByID(post.postID!!, requireContext())
                        Toast.makeText(context,"Deleted post",Toast.LENGTH_LONG).show()
                        view.findViewById<Button>(R.id.refresh).performClick()
                    }

                    builder.setNegativeButton("No"){dialogInterface, which ->
                        Toast.makeText(context,"canceled",Toast.LENGTH_LONG).show()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    true
                }

                bookmark_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    val register = com.example.pangea.DatabaseHandler()
                    context?.let { it1 -> register.updatePostBookmarked(post.postID.toString(), isChecked, it1) }
                }

                linearLayout.addView(cardview)
            }
        }
        return view

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        val refreshButton = view?.findViewById<Button>(R.id.bookmarkedRefreshButton)
        if (refreshButton != null) {
            refreshButton.performClick()
        }
    }
}