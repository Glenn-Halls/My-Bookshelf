package com.example.mybookshelf.data

import com.example.mybookshelf.model.BestsellerResult
import com.example.mybookshelf.network.BestsellerApi

interface BestsellerRepository {
    suspend fun getBestsellers(): BestsellerResult
}

class NetworkBestsellerRepository() : BestsellerRepository {
    override suspend fun getBestsellers(): BestsellerResult {
        return BestsellerApi.retrofitService.getBestsellers()
    }
}
