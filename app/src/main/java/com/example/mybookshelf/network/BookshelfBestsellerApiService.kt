package com.example.mybookshelf.network

import com.example.mybookshelf.model.BestsellerSearch
import com.example.mybookshelf.model.NytListSearch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY = "Y1dmz28FXM3bGGP8dD48kU617uOzGYdt"

interface BookshelfBestsellerApiService {
    @GET("svc/books/v3/lists/current/{list}.json")
    suspend fun getBestsellers(
        @Path("list", encoded = true) nytList: String,
        @Query("api-key") key: String = API_KEY,
    ): BestsellerSearch
}

interface BookshelfNytListApiService{
    @GET("svc/books/v3//lists/names.json")
    suspend fun getNytBestsellerLists(
        @Query("api-key") key: String = API_KEY
    ): NytListSearch
}
