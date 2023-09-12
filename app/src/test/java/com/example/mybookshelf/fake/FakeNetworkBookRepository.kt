package com.example.mybookshelf.fake

import com.example.mybookshelf.data.BookRepository
import com.example.mybookshelf.model.SearchResult

class FakeNetworkBookRepository : BookRepository {
    override suspend fun getBooks(): SearchResult {
        return FakeBookDataSource.searchResult
    }
}
