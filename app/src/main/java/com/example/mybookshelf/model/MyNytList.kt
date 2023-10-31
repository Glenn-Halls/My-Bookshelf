package com.example.mybookshelf.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NYT_LIST")
data class MyNytList(
    @PrimaryKey
    val listName: String
)
