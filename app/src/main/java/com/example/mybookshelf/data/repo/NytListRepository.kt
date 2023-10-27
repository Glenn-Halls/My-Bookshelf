package com.example.mybookshelf.data.repo

import com.example.mybookshelf.model.NytListSearch
import com.example.mybookshelf.network.BookshelfNytListApiService

interface NytListRepository {
    suspend fun getNytLists(): NytListSearch
}

class NetworkNytListRepository(
    private val nytListApiService: BookshelfNytListApiService,
) : NytListRepository {
    override suspend fun getNytLists(): NytListSearch =
        nytListApiService.getNytBestsellerLists()
}
