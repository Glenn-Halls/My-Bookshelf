package com.example.mybookshelf.data.repo

import com.example.mybookshelf.data.api.MyNytListRepository
import com.example.mybookshelf.data.database.MyNytListDao
import com.example.mybookshelf.model.MyNytList
import kotlinx.coroutines.flow.Flow

class OfflineMyNytListRepository (private val myNytListDao: MyNytListDao)
    : MyNytListRepository {
    override suspend fun insertNytList(nytList: MyNytList) =
        myNytListDao.insert(nytList)

    override suspend fun deleteNytList(nytList: MyNytList) =
        myNytListDao.delete(nytList)

    override suspend fun getAllMyNytLists(): List<MyNytList>? =
        myNytListDao.getAllMyNytLists()

    override fun getMyNytListFlow(): Flow<List<MyNytList>> =
        myNytListDao.getMyNytListFlow()

}
