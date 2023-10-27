package com.example.mybookshelf.fake

import com.example.mybookshelf.data.api.MyBookRepository
import com.example.mybookshelf.model.MyBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeMyBookRepository : MyBookRepository {
    override suspend fun insertBook(book: MyBook) {
    }

    override suspend fun deleteBook(book: MyBook) {
    }

    override suspend fun updateBook(book: MyBook) {
    }

    override fun getAllBooksStream(): Flow<List<MyBook>> {
        return emptyFlow()
    }

}
