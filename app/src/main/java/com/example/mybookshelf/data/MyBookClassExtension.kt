package com.example.mybookshelf.data

import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.BookCover
import com.example.mybookshelf.model.BookDetail
import com.example.mybookshelf.model.MyBook

fun MyBook.convertToBook(): Book {
    return Book(
        id = id,
        link = link,
        bookDetail = BookDetail(
            title = title,
            date = "NOT IMPLEMENTED",
            description = description,
            bookCover = BookCover(
                thumbnail = thumbnail
            )
        )
    )
}
