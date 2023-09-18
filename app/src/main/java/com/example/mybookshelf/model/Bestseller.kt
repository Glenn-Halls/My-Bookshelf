package com.example.mybookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val NO_COVER_AVAILABLE = "https://i.imgur.com/YjoNXCX.png"

@Serializable
data class Bestseller(
    val rank: Int,
    @SerialName(value = "rank_last_week")
    val previousRank: Int,
    @SerialName(value = "weeks_on_list")
    val weeksOnList: Int,
    @SerialName(value = "primary_isbn13")
    val isbn13: String,
    val title: String,
    val author: String,
    val publisher: String,
    val description: String,
    val price: String,
    @SerialName(value = "book_image")
    val coverImage: String = NO_COVER_AVAILABLE
)

val FakeBestseller = Bestseller(
    rank = 99,
    previousRank = 98,
    weeksOnList = 99,
    isbn13 = "isbn 13",
    title = "title",
    author = "author",
    publisher = "publisher",
    description = "description",
    price = "price"
)
