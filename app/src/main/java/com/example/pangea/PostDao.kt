package com.example.pangea

import androidx.room.*

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAll(): List<Post>

    @Query("SELECT * FROM posts WHERE uid IN (:postIds)")
    fun loadAllByIds(postIds: IntArray): List<Post>

    @Query("SELECT * FROM posts WHERE email IS (:email)")
    fun selectAllPostsForUser(email: String): List<Post>

    @Query("SELECT * FROM posts WHERE postID = :postID")
    fun selectPostbyID(postID: String): Post

    @Update
    fun updatePost(vararg post : Post)

    @Insert
    fun insertAll(vararg posts: Post)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(post: Post)

    @Query("DELETE FROM posts WHERE postID = :postID")
    fun deletePostByID(postID: String)

    @Query("DELETE FROM posts WHERE email = :email AND message = :message AND facebook = 1")
    fun deleteFBPostByUserIdWitText(email: String, message: String)

    @Query("DELETE FROM posts WHERE email = :email AND message = :message AND twitter = 1")
    fun deleteTwitterPostByUserIdWitText(email: String, message: String)

    @Query("SELECT * FROM posts WHERE email IS (:email) AND bookmarked = 1")
    fun selectAllPostsForUserBookmarked(email: String): List<Post>
}