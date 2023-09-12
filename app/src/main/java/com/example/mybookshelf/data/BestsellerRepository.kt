package com.example.mybookshelf.data

import com.example.mybookshelf.model.BestsellerResult
import com.example.mybookshelf.network.BookshelfBestsellerApiService

interface BestsellerRepository {
    suspend fun getBestsellers(): BestsellerResult
}

class NetworkBestsellerRepository(
    private val bestsellerApiService: BookshelfBestsellerApiService
) : BestsellerRepository {
    override suspend fun getBestsellers(): BestsellerResult =
        bestsellerApiService.getBestsellers()

}
