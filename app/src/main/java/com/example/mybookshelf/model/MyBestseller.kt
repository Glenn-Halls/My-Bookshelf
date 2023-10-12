package com.example.mybookshelf.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BESTSELLER")
data class MyBestseller(
    @PrimaryKey
    val isbn13: String,
    val rank: Int,
    val previousRank: Int,
    val weeksOnList: Int,
    val title: String,
    val author: String,
    val publisher: String,
    val description: String,
    val coverImage: String,
)
