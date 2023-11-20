package com.example.mybookshelf.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.mybookshelf.DefaultAppContainer
import com.example.mybookshelf.data.api.DATABASE_WORKER_OUTPUT
import com.example.mybookshelf.data.api.NETWORK_WORKER_OUTPUT
import com.example.mybookshelf.data.repo.NetworkNytOverviewRepository
import com.example.mybookshelf.data.repo.NytOverviewRepository

private const val TAG = "NetworkUpdateWorker"

/*
    NetworkUpdateWorker will attempt to run ONLY if DatabaseDownloadWorker was
    successful in retrieving a list of MyBestsellers that need to be updated. In this case,
    it will download the entire NYT bestseller list and return an array of Isbn13s.
 */
class NetworkUpdateWorker (
    context: Context,
    params: WorkerParameters,

) : CoroutineWorker(context, params) {

    val dBArray = inputData.getStringArray(DATABASE_WORKER_OUTPUT)
    val dBIsbnList = dBArray?.toList() ?: emptyList()
    val appContainer = DefaultAppContainer(context)
    val nytOverviewRetrofit = appContainer.nytListOverviewRetrofitService
    val nytOverviewRepository: NytOverviewRepository by lazy {
        NetworkNytOverviewRepository(nytOverviewRetrofit)
    }


    override suspend fun doWork(): Result {
        if (dBArray.isNullOrEmpty()) {
            Log.d(TAG, "success, no books to update")
            val outputData = workDataOf(NETWORK_WORKER_OUTPUT to emptyArray<String>())
            return Result.success(outputData)
        } else try {
            val nytResponse = nytOverviewRepository.getNytOverview()
            val nytList = nytResponse.results.lists.flatMap { it.books }
            val listToUpdate = nytList.filter {
                it.isbn13 in dBIsbnList
            }
            val arrayList = listToUpdate.map { it.isbn13 }.toTypedArray()
            val arrayTitle = listToUpdate.map { it.title }.toTypedArray()
            updateDatabase(arrayList, arrayTitle)
            val outputData = workDataOf(NETWORK_WORKER_OUTPUT to arrayList)
            return Result.success(outputData)
        } catch (exception: Exception) {
            Log.d("Overview Exception:", exception.toString())
            return Result.failure()
        }
    }

    private fun updateDatabase(isbnList: Array<String>, titleList: Array<String>) {
        titleList.forEach {
            Log.d(TAG, "$it will be updated")
        }
    }
}
