package com.example.mybookshelf.data.api

import com.example.mybookshelf.model.MyBook
import kotlinx.coroutines.flow.Flow

interface MyBookRepository {
    // Add book to database
    suspend fun insertBook(book: MyBook)

    // Delete book from database
    suspend fun deleteBook(book: MyBook)

    // Update book in database
    suspend fun updateBook(book: MyBook)

    fun getAllBooksStream(): Flow<List<MyBook>>
}
