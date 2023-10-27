package com.example.mybookshelf.network

import com.example.mybookshelf.data.api.BestsellerRepository
import com.example.mybookshelf.model.BestsellerSearch

class NetworkBestsellerRepository(
    private val bestsellerApiService: BookshelfBestsellerApiService,
    private val nytList: String
) : BestsellerRepository {
    override suspend fun getBestsellers(): BestsellerSearch =
        bestsellerApiService.getBestsellers(nytList)

}
