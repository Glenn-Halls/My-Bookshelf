package com.example.mybookshelf.fake

import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.BestsellerResult
import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.SearchResult

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
        Bestseller(linkOne),
        Bestseller(linkTwo),
        Bestseller(linkThree)
    )

    val searchResult = SearchResult(
        totalItems = totalItems,
        items = items,
    )

    val bestsellerResult = BestsellerResult(
        totalItems = totalItems,
        results = results
    )
}
