package com.example.mybookshelf.data.api

import com.example.mybookshelf.model.MyNytList
import kotlinx.coroutines.flow.Flow

interface MyNytListRepository {
    suspend fun insertNytList(nytList: MyNytList)
    suspend fun deleteNytList(nytList: MyNytList)
    suspend fun getAllMyNytLists(): List<MyNytList>
    fun getMyNytListFlow(): Flow<List<MyNytList>>
}
