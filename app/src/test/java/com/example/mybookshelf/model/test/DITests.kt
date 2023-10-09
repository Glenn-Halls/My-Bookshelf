package com.example.mybookshelf.model.test

import com.example.mybookshelf.fake.FakeMyBookRepository
import com.example.mybookshelf.fake.FakeNetworkBestsellerRepository
import com.example.mybookshelf.fake.FakeNetworkBookRepository
import com.example.mybookshelf.fake.FakeNytListRepository
import com.example.mybookshelf.model.BookshelfViewModel
import com.example.mybookshelf.rules.TestDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DITests {

    val createViewModel = {
        BookshelfViewModel(
            bookRepository = FakeNetworkBookRepository(),
            bestsellerRepository = FakeNetworkBestsellerRepository(),
            nytListRepository = FakeNytListRepository(),
            myBookRepository = FakeMyBookRepository(),
        )
    }

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun dispatcherTest() =
        runTest {
            val one = 1
            assertEquals(1, one)
        }

    @Test
    fun testViewModel() =
        runTest {
            val viewModel = createViewModel()
            assertEquals(
                viewModel.uiState.value.searchResult?.items?.size,
                3
            )
        }


    @Test
    fun viewModelTest() =
        runTest {
            val viewModel = BookshelfViewModel(
                FakeNetworkBookRepository(),
                FakeNetworkBestsellerRepository(),
                FakeNytListRepository(),
                FakeMyBookRepository()
            )
            assertEquals(
                viewModel.uiState.value.searchResult?.items?.size,
                3
            )
        }
}
