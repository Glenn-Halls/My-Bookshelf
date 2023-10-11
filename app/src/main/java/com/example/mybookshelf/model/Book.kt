package com.example.mybookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String,
    @SerialName(value = "selfLink")
    val link: String,
    @SerialName(value = "volumeInfo")
    val bookDetail: BookDetail = BookDetail(),
)
