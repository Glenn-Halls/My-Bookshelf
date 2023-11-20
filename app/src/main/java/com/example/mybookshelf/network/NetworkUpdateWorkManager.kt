package com.example.mybookshelf.network

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mybookshelf.workers.DatabaseDownloadWorker
import com.example.mybookshelf.workers.DatabaseUpdateWorker
import com.example.mybookshelf.workers.NetworkUpdateWorker

class NetworkUpdateWorkManager(
    context: Context,
) {
    private val workManager = WorkManager.getInstance(context)

    fun startWork() {

        var continuation =
            workManager.beginWith(OneTimeWorkRequest.Companion.from(DatabaseDownloadWorker::class.java))

        val networkUpdater = OneTimeWorkRequestBuilder<NetworkUpdateWorker>().setConstraints(
            Constraints(
                requiredNetworkType = NetworkType.CONNECTED,
                requiresCharging = true,
                requiresDeviceIdle = true
            )
        )

        continuation = continuation.then(networkUpdater.build())

        val databaseUpdateWorker = OneTimeWorkRequestBuilder<DatabaseUpdateWorker>()

        continuation = continuation.then(databaseUpdateWorker.build())

        continuation.enqueue()

    }


}
