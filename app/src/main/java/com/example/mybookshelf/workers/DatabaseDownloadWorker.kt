package com.example.mybookshelf.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.mybookshelf.data.api.DATABASE_WORKER_OUTPUT
import com.example.mybookshelf.data.database.AppDatabase
import com.example.mybookshelf.model.MyBestseller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "DatabaseDownloadWorker"


/*
    DatabaseDownloadWorker will attempt to download a list of MyBestsellers from the database.
    If successful in retrieval, will return an array of Strings containing the ISBN13 values.
    If successful but no items found, will return an empty array.
    If unsuccessful, will result in failure
 */

class DatabaseDownloadWorker (
    private val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val myBestsellers: List<MyBestseller>? =
                    AppDatabase.getDatabase(context).myBestsellerDao().getMyBestsellersList()
                val myBestsellerIds: List<String>? = myBestsellers?.map { it.isbn13 }
                val myBestsellerArray = myBestsellerIds.orEmpty().toTypedArray()
                val outputData = workDataOf(DATABASE_WORKER_OUTPUT to myBestsellerArray)
                Log.e(TAG, "MyBestsellers list downloaded from database, " +
                        "${myBestsellerArray.size} items found.")
                if (myBestsellerArray.isEmpty()) {
                    Log.e(TAG, "MyBestsellerList is empty")
                }
                Result.success(outputData)
            } catch (exception: Exception) {
                Log.e(TAG, "Error downloading MyNytList from Database")
                Log.e(TAG, exception.toString())
                Result.failure()
            }
        }
    }
}
