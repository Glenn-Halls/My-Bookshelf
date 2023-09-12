package com.example.mybookshelf.data

import com.example.mybookshelf.model.SearchResult
import com.example.mybookshelf.network.BookshelfApi

interface BookRepository {
    suspend fun getBooks(): SearchResult
}

class NetworkBookRepository() : BookRepository {
    override suspend fun getBooks(): SearchResult {
        return BookshelfApi.retrofitService.getBooks()
    }
}
