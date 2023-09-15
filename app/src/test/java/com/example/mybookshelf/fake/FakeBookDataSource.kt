package com.example.mybookshelf.fake

import com.example.mybookshelf.model.BestsellerResults
import com.example.mybookshelf.model.BestsellerSearch
import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.BookSearchResult
import com.example.mybookshelf.model.FakeBestseller

object FakeBookDataSource {
    const val totalItems = 3

    const val idOne = "01"
    const val idTwo = "02"
    const val idThree = "03"
    const val linkOne = "url.01"
    const val linkTwo = "url.02"
    const val linkThree = "url.03"
    val items = listOf(
        Book(idOne, linkOne),
        Book(idTwo, linkTwo),
        Book(idThree, linkThree)
    )
    val results = listOf(
        FakeBestseller,
        FakeBestseller,
        FakeBestseller
    )

    val searchResult = BookSearchResult(
        totalItems = totalItems,
        items = items,
    )

    val bestsellerSearch = BestsellerSearch(
        totalItems = totalItems,
        results = BestsellerResults(
            "Made-up list",
            "Made-up date",
            "Made-up description",
            results
        )
    )
}
