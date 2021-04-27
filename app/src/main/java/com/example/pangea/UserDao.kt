package com.example.pangea

import androidx.room.*
import androidx.room.Dao

@Dao
interface UserDao {

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE email IS (:email)")
    fun getUserByEmail(email: String): User

    @Update
    fun updateUser(vararg user : User)

    @Insert
    fun insertAll(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(user: User)

    @Delete
    fun delete(user: User)
}