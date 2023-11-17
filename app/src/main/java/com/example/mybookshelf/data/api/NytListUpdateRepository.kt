package com.example.mybookshelf.data.api

import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.MyBestseller

const val DATABASE_WORKER_OUTPUT = "DatabaseOutput"
const val NETWORK_WORKER_OUTPUT = "NetworkOutput"

interface NytListUpdateRepository {
    fun getListFromDb()
    fun getListFromNetwork()
    fun checkUpdateRequired(dbList: List<MyBestseller>?, networkList: List<Bestseller>?): Boolean
    fun updateList()
    fun cancelWork()
}
