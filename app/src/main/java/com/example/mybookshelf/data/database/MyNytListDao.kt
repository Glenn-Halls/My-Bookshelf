package com.example.mybookshelf.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mybookshelf.model.MyNytList
import kotlinx.coroutines.flow.Flow

@Dao
interface MyNytListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(myNytList: MyNytList)

    @Delete
    suspend fun delete(myNytList: MyNytList)

    @Query("SELECT * from nyt_list")
    suspend fun getAllMyNytLists(): List<MyNytList>?

    @Query("SELECT * from nyt_list")
    fun getMyNytListFlow(): Flow<List<MyNytList>>
}
