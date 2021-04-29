package com.example.pangea

import android.content.Context

class DatabaseHandler {
     fun registerUser(userEmail: String, userPassword: String, context: Context): Int {
            val db = AppDatabase.getInstance(context)
            val userDao = db.userDao()
            userDao.insertOne(User(email = userEmail, password = userPassword,
                twitterAuthToken = null, twitterAuthSecret = null, facebookAuthToken = null
            ))
            return 0

    }

    fun getRegisteredUser(userEmail: String, context: Context): User {
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDao()
        val user = userDao.getUserByEmail(userEmail)
        return user
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
}