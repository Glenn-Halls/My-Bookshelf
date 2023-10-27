package com.example.mybookshelf.data.repo

import com.example.mybookshelf.data.database.MyBookDao
import com.example.mybookshelf.data.api.MyBookRepository
import com.example.mybookshelf.model.MyBook
import kotlinx.coroutines.flow.Flow

class OfflineMyBookRepository(private val myBookDao: MyBookDao) : MyBookRepository {
    override suspend fun insertBook(book: MyBook) = myBookDao.insert(book)
    override suspend fun deleteBook(book: MyBook) = myBookDao.delete(book)
    override suspend fun updateBook(book: MyBook) = myBookDao.update(book)
    override fun getAllBooksStream(): Flow<List<MyBook>> = myBookDao.getBooks()
}
