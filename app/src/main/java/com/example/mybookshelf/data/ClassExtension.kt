package com.example.mybookshelf.data

import com.example.mybookshelf.model.Book

fun Book.getShortDescription(charLength: Int = 0): String {
    val toString = bookDetail?.description.toString()
    val firstWord = toString.takeWhile { it != ' ' }

    // If character length is less than 1, return empty string.
    return if (charLength < 1) {
        ""
        // When string is longer than character length return shortened string.
    } else if (toString.length > charLength) {
        if (firstWord.length > charLength - 4) {
            toString.takeWhile { it != ' ' }
        } else {
            toString
                .take(charLength - 3)
                .replaceAfterLast(' ', "")
                .dropLast(1) + "..."
        }
        // When string is shorter than character length, then return original string.
    } else {
        toString
    }
}
