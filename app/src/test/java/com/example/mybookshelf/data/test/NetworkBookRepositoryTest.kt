package com.example.mybookshelf.data.test

import com.example.mybookshelf.data.NetworkBookRepository
import com.example.mybookshelf.fake.FakeBookApiService
import com.example.mybookshelf.fake.FakeBookDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkBookRepositoryTest {
    @Test
    fun networkBookRepository_getBooks_verifyBookList() =
        runTest {
        val repository = NetworkBookRepository(
            bookshelfApiService = FakeBookApiService()
        )
        assertEquals(FakeBookDataSource.items, repository.getBooks().items)
    }
}
