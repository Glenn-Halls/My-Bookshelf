package com.example.mybookshelf.data.test

import com.example.mybookshelf.network.NetworkBestsellerRepository
import com.example.mybookshelf.fake.FakeBestsellerApiService
import com.example.mybookshelf.fake.FakeBookDataSource
import com.example.mybookshelf.fake.FakeNytListApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkBestsellerRepositoryTest {
    @Test
    fun networkBestsellerRepository_getBestsellers_verifyBestsellerList() =
        runTest {
            val repository = NetworkBestsellerRepository(
                bestsellerApiService = FakeBestsellerApiService(),
                nytList = FakeNytListApiService().getNytBestsellerLists().results.size.toString()
            )
            assertEquals(
                FakeBookDataSource.bestsellerSearch.results,
                repository.getBestsellers().results
            )
        }
}
