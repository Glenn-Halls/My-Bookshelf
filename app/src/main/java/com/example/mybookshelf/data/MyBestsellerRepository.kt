package com.example.mybookshelf.data

import com.example.mybookshelf.model.MyBestseller
import kotlinx.coroutines.flow.Flow

interface MyBestsellerRepository {

    suspend fun insertBestseller(bestseller: MyBestseller)
    suspend fun deleteBestseller(bestseller: MyBestseller)
    fun getAllBestsellersStream(): Flow<List<MyBestseller>>
}
