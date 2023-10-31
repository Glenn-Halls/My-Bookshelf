package com.example.mybookshelf

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.mybookshelf.data.api.BestsellerRepository
import com.example.mybookshelf.data.api.BookRepository
import com.example.mybookshelf.data.api.DataStoreRepository
import com.example.mybookshelf.data.api.MyBestsellerRepository
import com.example.mybookshelf.data.api.MyBookRepository
import com.example.mybookshelf.data.api.MyNytListRepository
import com.example.mybookshelf.data.database.AppDatabase
import com.example.mybookshelf.data.database.DataStoreSerializer
import com.example.mybookshelf.data.repo.NetworkNytListRepository
import com.example.mybookshelf.data.repo.NytListRepository
import com.example.mybookshelf.data.repo.OfflineMyBestsellerRepository
import com.example.mybookshelf.data.repo.OfflineMyBookRepository
import com.example.mybookshelf.data.repo.OfflineMyNytListRepository
import com.example.mybookshelf.data.repo.ProtoDataStoreRepository
import com.example.mybookshelf.network.BookApiService
import com.example.mybookshelf.network.BookshelfBestsellerApiService
import com.example.mybookshelf.network.BookshelfNytListApiService
import com.example.mybookshelf.network.NetworkBestsellerRepository
import com.example.mybookshelf.network.NetworkBookRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private val Context.dataStore: DataStore<ProtoData> by dataStore(
    fileName = "settings.pb",
    serializer = DataStoreSerializer
)

interface AppContainer {
    val bookRepository: BookRepository
    val bestsellerRepository: BestsellerRepository
    val nytListRepository: NytListRepository
    val myBookRepository: MyBookRepository
    val myBestsellerRepository: MyBestsellerRepository
    val myNytListRepository: MyNytListRepository
    val protoDataRepository: DataStoreRepository
    var searchString: String
    var nytListAddress: String
}

class DefaultAppContainer(
    private val context: Context,
    override var searchString: String = "Jazz",
    override var nytListAddress: String = "hardcover-fiction"
) : AppContainer {
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

    val bookRetrofitService: BookApiService by lazy {
        bookRetrofit.create(BookApiService::class.java)
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

    override val myNytListRepository: MyNytListRepository by lazy {
        OfflineMyNytListRepository(AppDatabase.getDatabase(context).myNytListDao())
    }

    override val protoDataRepository: DataStoreRepository by lazy {
        ProtoDataStoreRepository(context.dataStore)
    }
}
