package com.example.pangea

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class DatabaseHandler
{
     var all_connected_accounts = mutableListOf<SocialMediaAccounts>()


     fun registerUser(userEmail: String, userPassword: String, context: Context): Int {
            val db = AppDatabase.getInstance(context)
            val userDao = db.userDao()
            userDao.insertOne(User(email = userEmail, password = userPassword,
                twitterAuthToken = null, twitterAuthSecret = null, facebookAuthToken = null,
                language = "en", darkMode = false
            ))
            return 0

    }

    fun getRegisteredUser(userEmail: String, context: Context): User {
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDao()
        val user = userDao.getUserByEmail(userEmail)
        return user
    }

    fun changePassword(userEmail: String, userpassword: String, context: Context) {
        val user = getRegisteredUser(userEmail, context)
        user.password = userpassword
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDao()
        userDao.updateUser(user)
    }
    fun saveTwitterLink(user: User, twitterAuthToken: String?, twitterAuthTokenSecret: String?, context: Context) {
        val db = AppDatabase.getInstance(context)
        user.twitterAuthToken = twitterAuthToken
        user.twitterAuthSecret = twitterAuthTokenSecret
        db.userDao().updateUser(user)
    }

    fun saveFacebookLink(user: User, facebookOauthToken: String?, context: Context) {
        val db = AppDatabase.getInstance(context)
        user.facebookAuthToken = facebookOauthToken
        db.userDao().updateUser(user)
    }

    fun updateUserLanguage(userEmail: String, lang: String, context: Context) {
        val db = AppDatabase.getInstance(context)
        val user = getRegisteredUser(userEmail, context)
        user.language = lang
        db.userDao().updateUser(user)
    }

    fun updateUserTheme(userEmail: String, darkMode: Boolean, context: Context) {
        val db = AppDatabase.getInstance(context)
        val user = getRegisteredUser(userEmail, context)
        user.darkMode = darkMode
        db.userDao().updateUser(user)
    }

    fun getAllPosts(userEmail: String, context: Context): List<Post> {
        val db = PostDatabase.getInstance(context)
        val postDao = db.postDao()
        return postDao.selectAllPostsForUser(userEmail)
    }

    fun addFBPost(userEmail: String, message: String, image: String?, context: Context, id: String?): Int {
        val db = PostDatabase.getInstance(context)
        val postDao = db.postDao()
        val post = Post(email = userEmail, message = message, image =  image, facebook = true, twitter = false, postID = id)
        postDao.insertOne(post)
        return 0
    }

    fun addTwitterPost(userEmail: String, message: String, image: String?, context: Context, id: String?): Int {
        val db = PostDatabase.getInstance(context)
        val postDao = db.postDao()
        val post = Post(email = userEmail, message = message, image =  image, facebook =  false, twitter = true, postID = id)
        postDao.insertOne(post)
        return 0
    }

    //add image if needed
    fun deletePost(userEmail: String, message: String, image: String?, accountType: String, context: Context) {
        val db = PostDatabase.getInstance(context)
        val postDao = db.postDao()
        if(accountType.equals("Facebook")) {
            postDao.deleteFBPostByUserIdWitText(userEmail, message)
        }
        else if(accountType.equals("Twitter")) {
            postDao.deleteTwitterPostByUserIdWitText(userEmail, message)
        }
    }

    fun deletePostByID(postID: String, context: Context)
    {
        val db = PostDatabase.getInstance(context)
        val postDao = db.postDao()

        postDao.deletePostByID(postID)
    }

    fun getAllSocialMediaAccounts(context: Context): List<SocialMediaAccounts> {
        val db = SocialMediaAccountsDatabase.getInstance(context)
        val social_dao = db.socialMediaDao()
        return social_dao.getAll()
    }

    fun addSocialMediaAccount(socialMediaAccounts: SocialMediaAccounts, context: Context)
    {
        val db = SocialMediaAccountsDatabase.getInstance(context)
        val socialDao = db.socialMediaDao()
        socialDao.insertOne(socialMediaAccounts)
    }
}