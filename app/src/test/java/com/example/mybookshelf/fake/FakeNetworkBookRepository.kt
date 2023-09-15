package com.example.mybookshelf.fake

import com.example.mybookshelf.data.BookRepository
import com.example.mybookshelf.model.BookSearchResult

class FakeNetworkBookRepository : BookRepository {
    override suspend fun getBooks(): BookSearchResult {
        return FakeBookDataSource.searchResult
    }
}
