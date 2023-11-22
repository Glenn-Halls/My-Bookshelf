package com.example.mybookshelf.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mybookshelf.DefaultAppContainer
import com.example.mybookshelf.data.database.AppDatabase
import com.example.mybookshelf.data.repo.NetworkNytOverviewRepository
import com.example.mybookshelf.data.repo.NytOverviewRepository
import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.MyBestseller
import com.example.mybookshelf.model.extension.convertToMyBestseller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

private const val TAG = "PeriodicUpdateWorker"

class PeriodicUpdateWorker (
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    val database = AppDatabase.getDatabase(context).myBestsellerDao()
    private val nytOverviewRetrofit = DefaultAppContainer(context).nytListOverviewRetrofitService
    private val nytOverviewRepository: NytOverviewRepository by lazy {
        NetworkNytOverviewRepository(nytOverviewRetrofit)
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val myBestsellerList = async { databaseDownload() }
                val nytBestsellerList = async { networkDownload() }
                val updateList = getUpdateList(
                    myBestsellerList.await(),
                    nytBestsellerList.await()
                )
                updateDatabase(updateList)
                Result.success()
            } catch (exception: Exception) {
                Log.e(TAG, "Periodic DoWork() failed")
                Result.failure()
            }
        }
    }


    private fun databaseDownload(): List<MyBestseller> {
        return try {
            Log.d(TAG, "Fetching Bestsellers from database")
            database.getMyBestsellersList() ?: emptyList()
        } catch (exception: Exception) {
            Log.e(TAG, "Error downloading from MyBestseller Database")
            emptyList()
        }
    }

    private suspend fun networkDownload(): List<Bestseller> {
        return try {
            Log.d(TAG, "Fetching Bestsellers from network")
            val nytResponse = nytOverviewRepository.getNytOverview()
            nytResponse.results.lists.flatMap { it.books }
        } catch (exception: Exception) {
            Log.e(TAG, "Failure on Bestseller network download")
            emptyList()
        }
    }

    private fun getUpdateList(
        myBestsellers: List<MyBestseller>,
        bestsellers: List<Bestseller>
    ): List<Bestseller> {
        return bestsellers.filter {it.isbn13 in myBestsellers.map { it.isbn13 }}
    }

    private suspend fun updateDatabase(bestsellers: List<Bestseller>) {
        try {
            Log.d(TAG, "Updating Database")
            bestsellers.forEach {
                database.insert(it.convertToMyBestseller())
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Error writing Bestsellers to Database")
        }
    }
}
