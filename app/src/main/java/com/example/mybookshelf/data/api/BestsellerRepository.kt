package com.example.mybookshelf.data.api

import com.example.mybookshelf.model.BestsellerSearch

interface BestsellerRepository {
    suspend fun getBestsellers(): BestsellerSearch
}
