package com.example.pangea

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DatabaseHandler {
    public fun registerUser(userEmail: String, userPassword: String, context: Context): Int {
            val db = AppDatabase.getInstance(context)
            val userDao = db.userDao()
            userDao.insertOne(User(email = userEmail, password = userPassword))
            return 0

        }
    public fun getRegisteredUser(userEmail: String, context: Context): User {
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDao()
        val user = userDao.getUserByEmail(userEmail)
        return user
    }

    public fun getAllPosts(userEmail: String, context: Context): List<Post?> {
        return listOf()
    }

    public fun addPost(userEmail: String, message: String, image: String?, platforms: List<Boolean>, context: Context): Post? {
        return null
    }

}