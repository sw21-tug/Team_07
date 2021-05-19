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
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.posts_view.view.*
import kotlinx.android.synthetic.main.single_post.*


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

                linearLayout.addView(cardview)

//                var imageParams: RelativeLayout.LayoutParams
//                imageParams = RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT
//                )
//
//                imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//                val img = ImageView(context)
//                img.layoutParams = imageParams
//
//                if(post.facebook) {
//                    img.setImageResource(R.drawable.facebookiconpreview)
//                }
//                else {
//                    img.setImageResource(R.drawable.twitter_bird_logo_2012_svg)
//                }
//                linearLayout.addView(img)
//
//
//                val cardView = activity?.applicationContext?.let { CardView(it) }
//                if (cardView != null) {
//                    cardView.minimumWidth = 300
//                    cardView.minimumHeight = 300
//                    cardView.setContentPadding(15, 0, 15, 15)
//                    cardView.setCardBackgroundColor(Color.LTGRAY)
//                    cardView.radius = 20f
//                }
//
////                  USE THIS IF CHECKBOX DOESN'T WORK
////                val bookmarked_btn = ImageButton(activity?.applicationContext)
////                bookmarked_btn.setImageDrawable(
////                    ContextCompat.getDrawable(
////                        requireActivity().applicationContext, // Context
////                        android.R.drawable.btn_star // Drawable
////                    )
////                )
//
//                val bookmark_checkbox = CheckBox(activity?.applicationContext)
//                bookmark_checkbox.setButtonDrawable(
//                    ContextCompat.getDrawable(
//                    requireActivity().applicationContext, // Context
//                    android.R.drawable.btn_star // Drawable
//                ))
//
//                val textView = TextView(activity?.applicationContext)
//                textView.text = post.message
//
//                textView.textSize = 18F
//                textView.setTextColor(Color.DKGRAY)
//                cardView?.addView(textView)
//                cardView?.addView(bookmark_checkbox)
//                val bookmark = cardView?.getChildAt(1)
//                bookmark!!.measure(10,10)
//                //bookmark!!.accessibilityLiveRegion
//                bookmark!!.layout(0, 0, 0, 0)
//                bookmark!!.alpha = 0.5F
//
//                linearLayout.addView(cardView)
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
                view?.sendpostbtn?.setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
                view?.sendpostbtn?.setEnabled(true)
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//
//        val sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
//        val email = sharedPref.getString("current_user", "").toString()
//        val register = DatabaseHandler()
//
//        val curr_user: User? = context?.let { register.getRegisteredUser(email, it) }
//
//        val fhandler = FacebookHandler(requireContext(), curr_user!!, activity)
//
//        //fhandler.initApi(this)
//        val thandler = TwitterHandler(requireContext(), curr_user)
//        //thandler.initTwitterApi()
//        val hasFAccount = fhandler.hasLinkedAccount()
//        val hasTAccount = thandler.hasLinkedAccount()
//
//        if (!hasFAccount && !hasTAccount) {
//
//            view?.sendpostbtn?.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
//
//        }
//
//        else{
//            view?.sendpostbtn?.setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
//        }
//    }
}