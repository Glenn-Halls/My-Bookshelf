package com.example.mybookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val NO_COVER_IMAGE = "https://i.imgur.com/YjoNXCX.png"
private const val NO_DATE = "Date not available"
private const val NO_DESCRIPTION = "Description not available"
private const val NO_TITLE = "Title not available"
private val NO_AUTHOR = listOf("Author Unknown")

@Serializable
data class BookDetail(
    val title: String = NO_TITLE,
    @SerialName(value = "authors")
    val authors: List<String> = NO_AUTHOR,
    @SerialName(value = "publishedDate")
    val date: String = NO_DATE,
    val description: String = NO_DESCRIPTION,
    @SerialName(value = "imageLinks")
    val bookCover: BookCover = BookCover()
)

@Serializable
data class BookCover(
    val smallThumbnail: String = NO_COVER_IMAGE,
    val thumbnail: String = NO_COVER_IMAGE,
)

fun Book.getMissingInformation(bestseller: Bestseller): Book {
    this.bookDetail.let {
        val title = if (it.title == NO_TITLE) bestseller.title else it.title
        val authors = if (it.authors == NO_AUTHOR) listOf(bestseller.author) else it.authors
        val description = if (it.description == NO_DESCRIPTION) bestseller.description else it.description
        val bookCover = if (it.bookCover.thumbnail == NO_COVER_IMAGE) {
            BookCover(
                smallThumbnail = bestseller.coverImage,
                thumbnail = bestseller.coverImage
            )
        } else {
            it.bookCover
        }
        return this.copy(
            bookDetail = BookDetail(
                title = title,
                authors = authors,
                description = description,
                bookCover = bookCover
            )
        )
    }
}
