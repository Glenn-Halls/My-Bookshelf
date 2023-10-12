package com.example.mybookshelf.data

import android.content.Context
import com.example.mybookshelf.network.BookshelfApiService
import com.example.mybookshelf.network.BookshelfBestsellerApiService
import com.example.mybookshelf.network.BookshelfNytListApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bookRepository: BookRepository
    val bestsellerRepository: BestsellerRepository
    val nytListRepository: NytListRepository
    val myBookRepository: MyBookRepository
    val myBestsellerRepository: MyBestsellerRepository
    var searchString: String
    var nytListAddress: String
}

class DefaultAppContainer(
    private val context: Context,
    override var searchString: String = "Jazz",
    override var nytListAddress: String = "hardcover-fiction"
) : AppContainer{
    // Base details for Google's book API
    private val bookBaseUrl = "https://www.googleapis.com"
    private val bookJson = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // Base details for NYT's bestseller API
    private val bestsellerBaseUrl = "https://api.nytimes.com"
    private val bestsellerJson = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }


    /**
     * Use the Retrofit builder to build a Retrofit object to access Google API
     */
    private val bookRetrofit = Retrofit.Builder()
        .addConverterFactory(bookJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(bookBaseUrl)
        .build()

    val bookRetrofitService: BookshelfApiService by lazy {
        bookRetrofit.create(BookshelfApiService::class.java)
    }
    override val bookRepository: BookRepository by lazy {
        NetworkBookRepository(bookRetrofitService, searchString)
    }

    /**
     * Use the Retrofit builder to build a Retrofit object to access NYT's API
     */
    private val bestsellerRetrofit = Retrofit.Builder()
        .addConverterFactory(bestsellerJson.asConverterFactory(
            "application/json".toMediaType()
        ))
        .baseUrl(bestsellerBaseUrl)
        .build()

    val bestsellerRetrofitService: BookshelfBestsellerApiService by lazy {
        bestsellerRetrofit.create(BookshelfBestsellerApiService::class.java)
    }

    override val bestsellerRepository: BestsellerRepository by lazy {
        NetworkBestsellerRepository(bestsellerRetrofitService, nytListAddress)
    }

    /**
     * Use the Retrofit builder to build a Retrofit object to access NYT lists
     */
    private val nytListRetrofit = Retrofit.Builder()
        .addConverterFactory(bestsellerJson.asConverterFactory(
            "application/json".toMediaType()
        ))
        .baseUrl(bestsellerBaseUrl)
        .build()

    val nytListRetrofitService: BookshelfNytListApiService by lazy {
        nytListRetrofit.create(BookshelfNytListApiService::class.java)
    }

    override val nytListRepository: NytListRepository by lazy {
        NetworkNytListRepository(nytListRetrofitService)
    }

    override val myBookRepository: MyBookRepository by lazy {
        OfflineMyBookRepository(AppDatabase.getDatabase(context).myBookDao())
    }

    override val myBestsellerRepository: MyBestsellerRepository by lazy {
        OfflineMyBestsellerRepository(AppDatabase.getDatabase(context).myBestsellerDao())
    }
}
