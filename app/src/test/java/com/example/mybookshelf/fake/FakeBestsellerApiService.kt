package com.example.mybookshelf.fake

import com.example.mybookshelf.model.BestsellerResult
import com.example.mybookshelf.network.BookshelfBestsellerApiService

class FakeBestsellerApiService : BookshelfBestsellerApiService {
    override suspend fun getBestsellers(): BestsellerResult {
        return BestsellerResult(
            totalItems = FakeBookDataSource.totalItems,
            results = FakeBookDataSource.results
        )
    }
}
