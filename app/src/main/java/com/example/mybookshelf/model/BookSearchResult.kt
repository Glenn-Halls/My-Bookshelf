package com.example.mybookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class BookSearchResult(
    val totalItems: Int,
    val items: List<Book>,
    var favouritesInSearch: List<String> = emptyList(),
    var myBooksInSearch: List<String> = emptyList()
)
