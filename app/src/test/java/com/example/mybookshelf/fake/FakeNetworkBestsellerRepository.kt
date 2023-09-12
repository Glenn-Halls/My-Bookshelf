package com.example.mybookshelf.fake

import com.example.mybookshelf.data.BestsellerRepository
import com.example.mybookshelf.model.BestsellerResult

class FakeNetworkBestsellerRepository : BestsellerRepository {
    override suspend fun getBestsellers(): BestsellerResult {
        return FakeBookDataSource.bestsellerResult
    }
}
