package com.example.mybookshelf.data.api

import com.example.mybookshelf.model.BookSearchResult

interface BookRepository {
    suspend fun getBooks(): BookSearchResult
}
