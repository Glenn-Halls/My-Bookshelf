package com.example.mybookshelf.data

import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.BookCover
import com.example.mybookshelf.model.BookDetail
import com.example.mybookshelf.model.MyBook
import com.example.mybookshelf.model.SortOrder

fun MyBook.convertToBook(): Book {
    return Book(
        id = id,
        link = link,
        bookDetail = BookDetail(
            title = title,
            date = date,
            description = description,
            bookCover = BookCover(
            thumbnail = thumbnail
            )
        )
    )
}

fun List<MyBook>.sortBooks(
    sortOrder: SortOrder
): List<MyBook> {
    return when (sortOrder) {
        SortOrder.ALPHABETICAL -> sortedBy{it.title}
        SortOrder.ALPHABETICAL_REVERSE -> sortedBy{it.title}
        SortOrder.LAST_UPDATED -> sortedBy{it.lastUpdated}.reversed()
        SortOrder.LAST_UPDATED_REVERSE -> sortedBy { it.lastUpdated }.reversed()
        SortOrder.LAST_ADDED -> reversed()
        SortOrder.LAST_ADDED_REVERSE -> filter { true }
    }
}
