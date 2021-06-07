package com.example.pangea

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.text.Editable
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.posts_popup.*
import java.io.File
import java.net.URI

class PostsPopup : AppCompatActivity(), TwitterHandler.ITwitterCallback, FacebookHandler.IFacebookCallback
{
    lateinit var image : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.posts_popup)

        if (Build.VERSION.SDK_INT > 9) {
            val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val register = DatabaseHandler()

        val userEmail = intent.getStringExtra("loggedInUserMail").toString()

        val curr_user: User = register.getRegisteredUser(userEmail, applicationContext)
        val fhandler = FacebookHandler(applicationContext, curr_user, this@PostsPopup)
        fhandler.initApi(this)
        val thandler = TwitterHandler(applicationContext, curr_user)
        thandler.initTwitterApi()
        var hasFAccount = fhandler.hasLinkedAccount()
        var hasTAccount = thandler.hasLinkedAccount()

        if (hasFAccount)
        {
            facebookCheck.setEnabled(true)
        }
        if (hasTAccount)
        {
            twitterCheck.setEnabled(true)
        }

        // facebook and twitter have booleans
        send.setOnClickListener {

            val message: Editable? = plain_text_input.text

            val facebook_check = facebookCheck.isChecked
            val twitter_check = twitterCheck.isChecked
//            facebookCheck.setEnabled(true)
//            twitterCheck.setEnabled(true)
            // save in database
            // TODO


            val register = DatabaseHandler()

            val userEmail = intent.getStringExtra("loggedInUserMail").toString()

            val curr_user: User = register.getRegisteredUser(userEmail, applicationContext)

            val fhandler = FacebookHandler(applicationContext, curr_user, this@PostsPopup)
            fhandler.initApi(this)
            val thandler = TwitterHandler(applicationContext, curr_user)
            thandler.initTwitterApi()

            // call facebook or twitter post message here

            if (facebook_check && twitter_check) {
                if(!this::image.isInitialized)
                {
                    image = ""
                }

                var postId = fhandler.postMessage(message.toString(), image)
                register.addFBPost(
                    userEmail,
                    message.toString(),
                    image,
                    applicationContext,
                    postId.toString()
                )
                var twitterId = thandler.postTweet(message.toString(), image)
                register.addTwitterPost(
                    userEmail,
                    message.toString(),
                    image,
                    applicationContext,
                    twitterId
                )
            }
            else if(facebook_check) {
                if(!this::image.isInitialized)
                {
                    image = ""
                }
                var postId = fhandler.postMessage(message.toString(), image)
                register.addFBPost(userEmail, message.toString(), image, applicationContext, postId.toString())
            }
            else if(twitter_check) {
                if(!this::image.isInitialized)
                {
                    image = ""
                }
                var postId = thandler.postTweet(message.toString(), image)
                register.addTwitterPost(userEmail, message.toString(), image, applicationContext, postId)
            }
            finish()
        }

        add_media_btn.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            preview_picture.setImageURI(data?.data)

            if(data != null)
            {
                image = getRealPathFromURI(data.data!!)
            }
        }
    }

    fun getRealPathFromURI(uri: Uri): String
    {
        val cursor: Cursor = getContentResolver().query(uri, null, null, null, null)!!

        cursor.moveToFirst()
        val idx: Int = cursor.getColumnIndex (MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    override fun oAuthResponse() {
        TODO("Not yet implemented")
    }

    override fun loggedOut() {
        TODO("Not yet implemented")
    }
}