package com.example.mybookshelf.fake

import com.example.mybookshelf.model.BookSearchResult
import com.example.mybookshelf.network.BookshelfApiService

class FakeBookApiService : BookshelfApiService {
    override suspend fun getBooks(
        searchString: String,
        results: String,
        key: String
    ): BookSearchResult {
        return BookSearchResult(
            totalItems = FakeBookDataSource.totalItems,
            items = FakeBookDataSource.items
        )
    }

}
