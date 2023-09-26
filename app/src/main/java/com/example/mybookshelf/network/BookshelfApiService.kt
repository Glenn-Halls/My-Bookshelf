package com.example.mybookshelf.network

import com.example.mybookshelf.model.BookSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface BookshelfApiService {
    @GET ("books/v1/volumes")
    suspend fun getBooks(
        @Query("q") searchString: String,
        @Query("maxResults") results: String,
        @Query("key") key: String = "AIzaSyBYvGg2AAjaTh91NSM8evzlLHWHTuZ3_R0",
    ): BookSearchResult
}
