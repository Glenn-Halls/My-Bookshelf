package com.example.mybookshelf.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://www.googleapis.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BookshelfApiService {
    @GET ("books/v1/volumes?q=jazz+history")
    suspend fun getBooks(): String
}

object BookshelfApi{
    val retrofitService: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }
}
