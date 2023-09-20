package com.example.mybookshelf.data

import com.example.mybookshelf.model.MyBook
import kotlinx.coroutines.flow.Flow

class OfflineMyBookRepository(private val myBookDao: MyBookDao) : MyBookRepository {
    override suspend fun insertBook(book: MyBook) = myBookDao.insert(book)
    override suspend fun deleteBook(book: MyBook) = myBookDao.delete(book)
    override suspend fun updateBook(book: MyBook) = myBookDao.update(book)
    override fun getAllBooksStream(): Flow<List<MyBook>> = myBookDao.getBooks()
}
