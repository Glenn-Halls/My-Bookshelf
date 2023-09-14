package com.example.mybookshelf.data.test

import com.example.mybookshelf.data.getShortDescription
import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.BookDetail
import org.junit.Assert.*
import org.junit.Test

class ClassExtensionTest {

    // Create two books, one with a short and one with a long description
    private val longString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    private val shortString = "Lorem ipsum."
    private val bookWithShortDescription = Book(
        id = "id",
        link = "link",
        bookDetail = BookDetail(description = shortString)
    )
    private val bookWithLongDescription = Book(
        id = "id",
        link = "link",
        bookDetail = BookDetail(description = longString)
    )

    @Test
    fun classExtension_getShortDescription_shortRemainsShort() {
        // Check that the short description remains the same length
        assertEquals(
            bookWithShortDescription.getShortDescription().length,
            bookWithShortDescription.bookDetail.description.length
        )
        // Check that the string is unchanged
        assertEquals(
            bookWithShortDescription.getShortDescription(),
            bookWithShortDescription.bookDetail.description
        )
    }

    @Test
    fun classExtension_getShortDescription_longBecomesShort() {
        // Check that the ORIGINAL long description is longer than 300 characters
        assertFalse(bookWithLongDescription.bookDetail.description.length <= 300)
        // Check that the long description is reduced in length
        assertTrue(
            bookWithLongDescription.getShortDescription(300).length <= 300
        )
        // Check that the long description is changed
        assertNotEquals(
            bookWithLongDescription.getShortDescription(),
            bookWithLongDescription.bookDetail.description
        )
        // Ensure that shortened version of description ends with "..."
        assertTrue(
            bookWithLongDescription.getShortDescription().endsWith("...")
        )
    }
}
