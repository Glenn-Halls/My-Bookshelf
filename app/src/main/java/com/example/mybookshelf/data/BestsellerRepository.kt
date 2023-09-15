package com.example.mybookshelf.data

import com.example.mybookshelf.model.BestsellerSearch
import com.example.mybookshelf.network.BookshelfBestsellerApiService

interface BestsellerRepository {
    suspend fun getBestsellers(): BestsellerSearch
}

class NetworkBestsellerRepository(
    private val bestsellerApiService: BookshelfBestsellerApiService
) : BestsellerRepository {
    override suspend fun getBestsellers(): BestsellerSearch =
        bestsellerApiService.getBestsellers()

}
