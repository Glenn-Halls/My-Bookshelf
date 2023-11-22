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

private const val TAG = "NetworkDownloadWorker"

/*
    NetworkDownloadWorker will attempt to run ONLY if DatabaseDownloadWorker was
    successful in retrieving a list of MyBestsellers that need to be updated. In this case,
    it will download the entire NYT bestseller list and return an array of Isbn13s.
 */
class NetworkDownloadWorker (
    context: Context,
    params: WorkerParameters,

) : CoroutineWorker(context, params) {

    private val dBArray = inputData.getStringArray(DATABASE_WORKER_OUTPUT)
    private val dBIsbnList = dBArray?.toList() ?: emptyList()
    private val appContainer = DefaultAppContainer(context)
    private val nytOverviewRetrofit = appContainer.nytListOverviewRetrofitService
    private val nytOverviewRepository: NytOverviewRepository by lazy {
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
