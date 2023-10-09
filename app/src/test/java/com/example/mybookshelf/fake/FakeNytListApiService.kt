package com.example.mybookshelf.fake

import com.example.mybookshelf.model.NytListSearch
import com.example.mybookshelf.network.BookshelfNytListApiService

class FakeNytListApiService : BookshelfNytListApiService {
    override suspend fun getNytBestsellerLists(key: String): NytListSearch {
        return NytListSearch(
            results = emptyList()
        )
    }
}
