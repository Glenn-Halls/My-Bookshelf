package com.example.mybookshelf.data

import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.MyBestseller
import com.example.mybookshelf.model.SortOrder

fun MyBestseller.getCoilUrl(): String {
    val oldUrl: String = coverImage
    return oldUrl.replace("http://", "https://")
}

fun MyBestseller.convertToBestseller() : Bestseller {
    return Bestseller(
        rank = rank,
        previousRank = previousRank,
        weeksOnList = weeksOnList,
        isbn13 = isbn13,
        title = title,
        author = author,
        publisher = publisher,
        description = description,
        price = "unknown",
        coverImage = coverImage
    )
}

fun List<MyBestseller>.sortBestsellers(
    sortOrder: SortOrder
): List<MyBestseller> {
    return when (sortOrder) {
        SortOrder.ALPHABETICAL -> sortedBy{it.title}
        SortOrder.ALPHABETICAL_REVERSE -> sortedBy{it.title}.reversed()
        SortOrder.LAST_UPDATED -> reversed()
        SortOrder.LAST_UPDATED_REVERSE -> filter { true }
        SortOrder.LAST_ADDED -> reversed()
        SortOrder.LAST_ADDED_REVERSE -> filter { true }
    }
}
