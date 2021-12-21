package com.example.stubprojectkotlin.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class User(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    @Ignore var name: String = "",
    var phone: String = ""
)