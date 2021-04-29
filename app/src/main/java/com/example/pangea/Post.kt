package com.example.pangea

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,

    @ColumnInfo(name = "email")
    val email: String?,

    @ColumnInfo(name = "message")
    val message: String?,

    @ColumnInfo(name = "image")
    val image: String?,

    @ColumnInfo(name = "facebook")
    val facebook: Boolean,

    @ColumnInfo(name = "twitter")
    val twitter: Boolean?,

    @ColumnInfo(name = "postID")
    val postID: String?

)