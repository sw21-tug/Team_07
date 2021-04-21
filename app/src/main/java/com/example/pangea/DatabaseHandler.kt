package com.example.pangea

import android.content.Context

class DatabaseHandler {
    public fun registerUser(userEmail: String, userPassword: String, context: Context): Int {
            val db = AppDatabase.getInstance(context)
            val userDao = db.userDao()
            userDao.insertOne(User(email = userEmail, password = userPassword,
                twitterAuthToken = "", twitterAuthSecret = ""
            ))
            return 0

        }
    public fun getRegisteredUser(userEmail: String, context: Context): User {
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDao()
        val user = userDao.getUserByEmail(userEmail)
        return user
    }

    fun saveTwitterLink(user: User, twitterAuthToken: String, twitterAuthTokenSecret: String, context: Context) {
        val db = AppDatabase.getInstance(context)
        user.twitterAuthToken = twitterAuthToken
        user.twitterAuthSecret = twitterAuthTokenSecret
        db.userDao().updateUser(user)
    }

}