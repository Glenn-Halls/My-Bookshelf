package com.example.mybookshelf.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BOOKSHELF")
data class MyBook(
    @PrimaryKey
    val id: String,
    val link: String,
    val title: String,
    val author: String,
    val description: String,
    val thumbnail: String,
    val rating: Int? = null,
    @ColumnInfo(name = "favourite")
    val isFavourite: Boolean = false,
    val notes: String? = null
)
