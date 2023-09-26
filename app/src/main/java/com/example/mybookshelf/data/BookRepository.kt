package com.example.mybookshelf.data

import com.example.mybookshelf.model.BookSearchResult
import com.example.mybookshelf.network.BookshelfApiService

interface BookRepository {
    suspend fun getBooks(): BookSearchResult
}

class NetworkBookRepository(
    private val bookshelfApiService: BookshelfApiService,
    private val searchString: String
) : BookRepository {
    override suspend fun getBooks(): BookSearchResult =
        bookshelfApiService.getBooks(searchString, "40")
}
