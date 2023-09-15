package com.example.mybookshelf.fake

import com.example.mybookshelf.model.BookSearchResult
import com.example.mybookshelf.network.BookshelfApiService

class FakeBookApiService : BookshelfApiService {
    override suspend fun getBooks(): BookSearchResult {
        return BookSearchResult(
            totalItems = FakeBookDataSource.totalItems,
            items = FakeBookDataSource.items
        )
    }

}
