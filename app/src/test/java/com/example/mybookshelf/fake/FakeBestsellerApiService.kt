package com.example.mybookshelf.fake

import com.example.mybookshelf.model.BestsellerSearch
import com.example.mybookshelf.network.BookshelfBestsellerApiService

class FakeBestsellerApiService : BookshelfBestsellerApiService {
    override suspend fun getBestsellers(
        nytList: String,
        key: String
    ): BestsellerSearch {
        return BestsellerSearch(
            totalItems = FakeBookDataSource.totalItems,
            results = FakeBookDataSource.bestsellerSearch.results
        )
    }
}
