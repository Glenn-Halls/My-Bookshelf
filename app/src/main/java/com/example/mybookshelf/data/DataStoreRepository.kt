package com.example.mybookshelf.data

import com.example.mybookshelf.ProtoData
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val dataStoreFlow: Flow<ProtoData>
    suspend fun setNumber(newNumber: Int)
}
