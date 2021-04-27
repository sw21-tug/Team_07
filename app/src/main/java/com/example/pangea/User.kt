package com.example.pangea

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
        @PrimaryKey(autoGenerate = true)
        val uid: Int = 0,

        @ColumnInfo(name = "email")
        val email: String?,

        @ColumnInfo(name = "password")
        val password: String?,

        @ColumnInfo(name = "twitterAuthToken")
        var twitterAuthToken: String?,

        @ColumnInfo(name = "twitterAuthSecret")
        var twitterAuthSecret: String?,

        @ColumnInfo(name = "facebookAuthToken")
        var facebookAuthToken: String?

)