package com.example.mybookshelf.data

import com.example.mybookshelf.model.SearchResult
import com.example.mybookshelf.network.BookshelfApiService

interface BookRepository {
    suspend fun getBooks(): SearchResult
}

class NetworkBookRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookRepository {
    override suspend fun getBooks(): SearchResult =
        bookshelfApiService.getBooks()
}
