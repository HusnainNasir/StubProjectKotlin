package com.example.stubprojectkotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stubprojectkotlin.db.dao.UserDao
import com.example.stubprojectkotlin.db.entites.User


@Database(entities = [User::class], version = 2)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object{
        const val DATABASE_NAME: String = "application_db"
    }
}