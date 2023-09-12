package com.example.mybookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val NO_COVER_AVAILABLE = "https://i.imgur.com/YjoNXCX.png"

@Serializable
data class BookDetail(
    val title: String? = "Title not available",
    @SerialName(value = "publishedDate")
    val date: String? = "Date not available",
    val description: String? = "Description not available",
    @SerialName(value = "imageLinks")
    val bookCover: String? = NO_COVER_AVAILABLE
)
