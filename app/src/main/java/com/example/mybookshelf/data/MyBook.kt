package com.example.mybookshelf.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bookshelf")
data class MyBook(
    @PrimaryKey
    val id: String,
    val link: String,
    val title: String,
    val author: String,
    val description: String,
    val thumbnail: String,
    val rating: Int = 0,
    @ColumnInfo(name = "favourite")
    val isFavourite: Boolean? = false,
    val notes: String? = null
)
