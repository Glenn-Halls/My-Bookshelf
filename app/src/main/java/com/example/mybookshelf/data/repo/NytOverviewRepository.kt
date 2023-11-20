package com.example.mybookshelf.data.repo

import com.example.mybookshelf.model.NytListOverview
import com.example.mybookshelf.network.BookshelfNytFullOverviewApiService

interface NytOverviewRepository{
    suspend fun getNytOverview(): NytListOverview
}

class NetworkNytOverviewRepository(
    private val nytOverviewApiService: BookshelfNytFullOverviewApiService
) : NytOverviewRepository {
    override suspend fun getNytOverview(): NytListOverview =
        nytOverviewApiService.getNytFullOverview()

}
