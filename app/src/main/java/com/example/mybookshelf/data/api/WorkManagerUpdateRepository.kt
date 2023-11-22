package com.example.mybookshelf.data.api

const val DATABASE_WORKER_OUTPUT = "DatabaseOutput"
const val NETWORK_WORKER_OUTPUT = "NetworkOutput"
const val UNIQUE_PERIODIC_WORK = "PeriodicWorkManager"

interface WorkManagerUpdateRepository{
    fun updateDatabase()
    fun enqueuePeriodicUpdates(numDays: Int)
}
