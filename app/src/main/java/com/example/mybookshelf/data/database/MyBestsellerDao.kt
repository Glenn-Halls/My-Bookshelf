package com.example.mybookshelf.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mybookshelf.model.MyBestseller
import kotlinx.coroutines.flow.Flow

@Dao
interface MyBestsellerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myBestseller: MyBestseller)

    @Delete
    suspend fun delete(myBestseller: MyBestseller)

    @Query("SELECT * from bestseller")
    fun getMyBestsellersList(): List<MyBestseller>?

    @Query("SELECT * from bestseller")
    fun getBestsellers(): Flow<List<MyBestseller>>
}
