package com.example.mybookshelf.network

import com.example.mybookshelf.model.SearchResult
import retrofit2.http.GET

interface BookshelfApiService {
    @GET ("books/v1/volumes?q=jazz+history")
    suspend fun getBooks(): SearchResult
}
