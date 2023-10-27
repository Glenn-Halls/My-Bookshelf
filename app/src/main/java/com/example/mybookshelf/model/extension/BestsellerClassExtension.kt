package com.example.mybookshelf.model.extension

import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.MyBestseller

fun Bestseller.convertToMyBestseller(): MyBestseller {
    return MyBestseller(
        isbn13 = isbn13,
        rank = rank,
        previousRank = previousRank,
        weeksOnList = weeksOnList,
        title = title,
        author = author,
        publisher = publisher,
        description = description,
        coverImage = coverImage
    )
}
