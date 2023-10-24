package com.example.mybookshelf.data

import com.example.mybookshelf.ProtoData
import com.example.mybookshelf.ProtoData.DarkMode
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val dataStoreFlow: Flow<ProtoData>
    suspend fun setNumber(newNumber: Int)
    suspend fun setDarkMode(mode: DarkMode)
}
