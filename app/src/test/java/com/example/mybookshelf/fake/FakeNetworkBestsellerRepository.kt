package com.example.mybookshelf.fake

import com.example.mybookshelf.data.api.BestsellerRepository
import com.example.mybookshelf.model.BestsellerSearch

class FakeNetworkBestsellerRepository : BestsellerRepository {
    override suspend fun getBestsellers(): BestsellerSearch {
        return FakeBookDataSource.bestsellerSearch
    }
}
