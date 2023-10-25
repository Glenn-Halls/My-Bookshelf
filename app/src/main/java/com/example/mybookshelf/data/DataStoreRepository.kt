package com.example.mybookshelf.data

import com.example.mybookshelf.ProtoData
import com.example.mybookshelf.ProtoData.DarkMode
import com.example.mybookshelf.ProtoData.ProtoScreenSelect
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val dataStoreFlow: Flow<ProtoData>
    suspend fun setSearchString(string: String)
    suspend fun getSearchString(): String
    suspend fun setDarkMode(mode: DarkMode)
    suspend fun setStartupScreen(protoStartupScreen: ProtoScreenSelect)
}
