package com.example.pangea

import android.content.Context

class DatabaseHandler {
     fun registerUser(userEmail: String, userPassword: String, context: Context): Int {
            val db = AppDatabase.getInstance(context)
            val userDao = db.userDao()
            userDao.insertOne(User(
                email = userEmail, password = userPassword,
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

}