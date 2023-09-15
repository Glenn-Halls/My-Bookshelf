package com.example.mybookshelf.data.test

import com.example.mybookshelf.data.NetworkBestsellerRepository
import com.example.mybookshelf.fake.FakeBestsellerApiService
import com.example.mybookshelf.fake.FakeBookDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkBestsellerRepositoryTest {
    @Test
    fun networkBestsellerRepository_getBestsellers_verifyBestsellerList() =
        runTest {
            val repository = NetworkBestsellerRepository(
                bestsellerApiService = FakeBestsellerApiService()
            )
            assertEquals(
                FakeBookDataSource.bestsellerSearch.results,
                repository.getBestsellers().results
            )
        }
}
