package com.example.mybookshelf.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mybookshelf.DefaultAppContainer
import com.example.mybookshelf.data.api.NETWORK_WORKER_OUTPUT
import com.example.mybookshelf.data.database.AppDatabase
import com.example.mybookshelf.data.repo.NetworkNytOverviewRepository
import com.example.mybookshelf.data.repo.NytOverviewRepository
import com.example.mybookshelf.model.extension.convertToMyBestseller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private const val TAG = "DatabaseUpdateWorker"

class DatabaseUpdateWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val updateIsbnList = inputData.getStringArray(NETWORK_WORKER_OUTPUT)
    private val appContainer = DefaultAppContainer(context)
    private val nytOverviewRetrofit = appContainer.nytListOverviewRetrofitService
    private val nytOverviewRepository: NytOverviewRepository by lazy {
        NetworkNytOverviewRepository(nytOverviewRetrofit)
    }


    override suspend fun doWork(): Result {
        if (updateIsbnList.isNullOrEmpty()) {
            Log.d(TAG, "No Bestsellers need update")
            return Result.success()
        } else try {
            withContext(Dispatchers.IO) {
                runBlocking {
                    val databaseDao = AppDatabase.getDatabase(context).myBestsellerDao()
                    val nytBookList = async {
                        nytOverviewRepository
                            .getNytOverview()
                            .results
                            .lists
                            .flatMap {
                                it.books
                            }
                    }
                    val myBestsellers = async { databaseDao.getMyBestsellersList() }
                    val updateList = nytBookList.await().filter {
                        it.isbn13 in (myBestsellers.await()?.map { it.isbn13 } ?: emptyList())
                    }
                    if (updateList.isNotEmpty()) {
                        updateList.forEach {
                            Log.d("Database Updating:", "${it.title} has been updated")
                            databaseDao.insert(it.convertToMyBestseller())
                        }
                    }
                }
            }
            return Result.success()
        } catch (exception: Exception) {
            return Result.failure()
        }
    }
}
