package com.example.mybookshelf.network

import com.example.mybookshelf.data.api.BookRepository
import com.example.mybookshelf.model.BookSearchResult

class NetworkBookRepository(
    private val bookApiService: BookApiService,
    private val searchString: String
) : BookRepository {
    override suspend fun getBooks(): BookSearchResult =
        bookApiService.getBooks(searchString, "40")
}
