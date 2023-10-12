package com.example.mybookshelf.data

import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.MyBestseller

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
