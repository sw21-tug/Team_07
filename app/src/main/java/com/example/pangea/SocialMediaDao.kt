package com.example.pangea

import androidx.room.*
import com.example.pangea.SocialMediaAccounts

@Dao
interface SocialMediaDao {
    @Query("SELECT * FROM SocialMediaAccounts")
    fun getAll(): List<SocialMediaAccounts>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(social: SocialMediaAccounts)

    @Query("DELETE FROM SocialMediaAccounts WHERE user_name = :userName")
    fun deleteByUserName(userName: String)

    /*@Query("DELETE FROM posts WHERE email = :email AND message = :message AND facebook = 1")
    fun deleteFBPostByUserIdWitText(email: String, message: String)

    @Query("DELETE FROM posts WHERE email = :email AND message = :message AND twitter = 1")
    fun deleteTwitterPostByUserIdWitText(email: String, message: String)*/
}