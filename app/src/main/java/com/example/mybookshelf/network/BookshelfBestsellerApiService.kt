package com.example.mybookshelf.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.nytimes.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BookshelfBestsellerApiService {
    @GET("svc/books/v3/reviews.json?author=Stephen+King&api-key=Y1dmz28FXM3bGGP8dD48kU617uOzGYdt")
    suspend fun getBestsellers(): String
}

object BestsellerApi{
    val retrofitService: BookshelfBestsellerApiService by lazy {
        retrofit.create(BookshelfBestsellerApiService::class.java)
    }
}
