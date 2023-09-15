package com.example.mybookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class BookSearchResult(
    val totalItems: Int,
    val items: List<Book>
)
