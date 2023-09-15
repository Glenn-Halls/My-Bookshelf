package com.example.mybookshelf.network

import com.example.mybookshelf.model.BookSearchResult
import retrofit2.http.GET

interface BookshelfApiService {
    @GET ("books/v1/volumes?q=jazz+history&maxResults=40")
    suspend fun getBooks(): BookSearchResult
}
