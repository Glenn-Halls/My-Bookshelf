package com.example.mybookshelf.network

import com.example.mybookshelf.model.BestsellerSearch
import com.example.mybookshelf.model.NytListSearch
import retrofit2.http.GET

interface BookshelfBestsellerApiService {
    @GET("svc/books/v3/lists/current/hardcover-fiction.json?api-key=Y1dmz28FXM3bGGP8dD48kU617uOzGYdt")
    suspend fun getBestsellers(): BestsellerSearch
}

interface BookshelfNytListApiService{
    @GET("svc/books/v3//lists/names.json?api-key=Y1dmz28FXM3bGGP8dD48kU617uOzGYdt")
    suspend fun getNytBestsellerLists(): NytListSearch
}
