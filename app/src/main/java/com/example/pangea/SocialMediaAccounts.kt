package com.example.pangea

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SocialMediaAccounts")
data class SocialMediaAccounts(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,

    @ColumnInfo(name = "user name")
    val user_name: String?,

    @ColumnInfo(name = "facebook")
    val facebook: Boolean,

    @ColumnInfo(name = "twitter")
    val twitter: Boolean?

)