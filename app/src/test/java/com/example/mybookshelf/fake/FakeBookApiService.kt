package com.example.mybookshelf.fake

import com.example.mybookshelf.model.SearchResult
import com.example.mybookshelf.network.BookshelfApiService

class FakeBookApiService : BookshelfApiService {
    override suspend fun getBooks(): SearchResult {
        return SearchResult(
            totalItems = FakeBookDataSource.totalItems,
            items = FakeBookDataSource.items
        )
    }

}
