package com.example.mybookshelf.data.repo

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mybookshelf.data.api.UNIQUE_PERIODIC_WORK
import com.example.mybookshelf.data.api.WorkManagerUpdateRepository
import com.example.mybookshelf.workers.DatabaseDownloadWorker
import com.example.mybookshelf.workers.DatabaseUpdateWorker
import com.example.mybookshelf.workers.NetworkDownloadWorker
import com.example.mybookshelf.workers.PeriodicUpdateWorker
import java.util.concurrent.TimeUnit

class NetworkUpdateWorkManager(
    context: Context,
) : WorkManagerUpdateRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun updateDatabase() = startWork()
    override fun enqueuePeriodicUpdates(numDays: Int) = enqueuePeriodicWork(numDays)


    fun startWork() {

        var continuation = workManager.beginWith(
                OneTimeWorkRequest.Companion.from(DatabaseDownloadWorker::class.java)
        )

        val networkUpdater = OneTimeWorkRequestBuilder<NetworkDownloadWorker>().setConstraints(
            Constraints(
                requiredNetworkType = NetworkType.CONNECTED,
            )
        )

        continuation = continuation.then(networkUpdater.build())

        val databaseUpdateWorker = OneTimeWorkRequestBuilder<DatabaseUpdateWorker>()

        continuation = continuation.then(databaseUpdateWorker.build())

        continuation.enqueue()

    }

    fun enqueuePeriodicWork(numDays: Int) {

        val constraints = Constraints(
            requiredNetworkType = NetworkType.CONNECTED,
            requiresCharging = false,
            requiresDeviceIdle = false,
            requiresBatteryNotLow = false,
        )

        val work =
            PeriodicWorkRequestBuilder<PeriodicUpdateWorker>(numDays.toLong(), TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(
            UNIQUE_PERIODIC_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }




}
