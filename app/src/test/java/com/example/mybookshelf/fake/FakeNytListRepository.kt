package com.example.mybookshelf.fake

import com.example.mybookshelf.data.NytListRepository
import com.example.mybookshelf.model.NytListSearch

class FakeNytListRepository : NytListRepository {
    override suspend fun getNytLists(): NytListSearch {
        return NytListSearch(emptyList())
    }

}
