package com.example.mybookshelf.network

import com.example.mybookshelf.model.BestsellerResult
import retrofit2.http.GET

interface BookshelfBestsellerApiService {
    @GET("svc/books/v3/reviews.json?author=Stephen+King&api-key=Y1dmz28FXM3bGGP8dD48kU617uOzGYdt")
    suspend fun getBestsellers(): BestsellerResult
}
