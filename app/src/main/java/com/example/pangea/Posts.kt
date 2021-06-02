package com.example.pangea


import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.posts_view.view.*
import kotlinx.android.synthetic.main.single_post.*
import kotlinx.android.synthetic.main.single_post.view.*


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
        val view = inflater.inflate(R.layout.posts_view, container, false)

        if (Build.VERSION.SDK_INT > 9) {
            val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }


        view.sendpostbtn.setOnClickListener {
            val intent = Intent(context, PostsPopup::class.java)
            if (email != null) {
                intent.putExtra("loggedInUserMail", email)
            }
                startActivity(intent)
        }

        view.refresh.setOnClickListener{
            if(!email.isNullOrEmpty())
            {
                posts = activity?.let { register.getAllPosts(email, it.applicationContext) }!!
            }

            val linearLayout : LinearLayout = view.findViewById(R.id.linearLayoutPosts)
            linearLayout.removeAllViews();
//            linearLayout.setDividerDrawable(
//                ContextCompat.getDrawable(
//                      requireActivity().applicationContext, // Context
//                      android.R.drawable.divider_horizontal_dark // Drawable
//                    )
//            )
//
//            linearLayout.dividerDrawable.setBounds(0, 0, 50, 50);
//
//            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_END)

            for (post in posts) {

                val layoutInflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val new_view = layoutInflater!!.inflate(R.layout.single_post, null)

                val cardview = new_view.findViewById<ConstraintLayout>(R.id.post_card_view)
                val textfield = new_view.findViewById<TextView>(R.id.post_text_field)
                val bookmark_checkbox = new_view.findViewById<CheckBox>(R.id.bookmark_checkbox)
                val posted_to_fb = new_view.findViewById<ImageView>(R.id.posted_to_fb)
                val posted_to_tw = new_view.findViewById<ImageView>(R.id.posted_to_tw)
                val post_date = new_view.findViewById<TextView>(R.id.post_date)
                bookmark_checkbox.isChecked = post.bookmarked

                textfield.text = post.message

                if(post.facebook)
                {
                    posted_to_fb.alpha = 1.0F
                }
                if(post.twitter == true)
                {
                    posted_to_tw.alpha = 1.0F
                }

                post_date.text = "01.01.1970"

                textfield.setOnClickListener {
                    val intent = Intent(context, PostExpanded::class.java)
                    intent.putExtra("Text", post.message)
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
                        //refresh directly??
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
        if (isVisibleToUser) {
            val sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
            val email = sharedPref.getString("current_user", "").toString()
            val register = DatabaseHandler()

            val curr_user: User? = context?.let { register.getRegisteredUser(email, it) }

            val fhandler = FacebookHandler(requireContext(), curr_user!!, activity)

            //fhandler.initApi(this)
            val thandler = TwitterHandler(requireContext(), curr_user)
            //thandler.initTwitterApi()
            val hasFAccount = fhandler.hasLinkedAccount()
            val hasTAccount = thandler.hasLinkedAccount()

            if (!hasFAccount && !hasTAccount) {

                view?.sendpostbtn?.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                view?.sendpostbtn?.setEnabled(false)

            }

            else{
                view?.sendpostbtn?.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                view?.sendpostbtn?.setEnabled(true)
            }
        }
    }
  


    override fun onResume() {
        super.onResume()
        val view = getView()
        if (view != null) {
            view.findViewById<Button>(R.id.refresh).performClick()
        }
    }


}