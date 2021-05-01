package com.example.pangea

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "users")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        fun destroyInstance() {
            if (INSTANCE?.isOpen == true) {
                INSTANCE?.close()
            }

            INSTANCE = null
        }
    }
}


@Database(entities = arrayOf(Post::class), version = 1)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: PostDatabase? = null

        fun getInstance(context: Context): PostDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PostDatabase::class.java,
                        "posts")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        fun destroyInstance() {
            if (INSTANCE?.isOpen == true) {
                INSTANCE?.close()
            }

            INSTANCE = null
        }
    }
}