package com.example.mybookshelf.data.repo

import com.example.mybookshelf.data.api.MyBestsellerRepository
import com.example.mybookshelf.data.database.MyBestsellerDao
import com.example.mybookshelf.model.MyBestseller
import kotlinx.coroutines.flow.Flow

class OfflineMyBestsellerRepository(private val myBestsellerDao: MyBestsellerDao)
    : MyBestsellerRepository {
    override suspend fun insertBestseller(bestseller: MyBestseller) =
        myBestsellerDao.insert(bestseller)

    override suspend fun deleteBestseller(bestseller: MyBestseller) =
        myBestsellerDao.delete(bestseller)

    override suspend fun getMyBestsellerList(): List<MyBestseller>? =
        myBestsellerDao.getMyBestsellersList()

    override fun getAllBestsellersStream(): Flow<List<MyBestseller>> =
        myBestsellerDao.getBestsellers()

}
