package com.example.mybookshelf.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mybookshelf.model.MyBook
import kotlinx.coroutines.flow.Flow

@Dao
interface MyBookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(myBook: MyBook)

    @Delete
    suspend fun delete(myBook: MyBook)

    @Update
    suspend fun update(myBook: MyBook)

    @Query("SELECT * from bookshelf ORDER BY :orderBy")
    fun getBooks(orderBy: String? = "title"): Flow<List<MyBook>>

}
