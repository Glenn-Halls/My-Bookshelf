package com.example.mybookshelf.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mybookshelf.model.MyBestseller
import com.example.mybookshelf.model.MyBook
import com.example.mybookshelf.model.MyNytList

@Database(entities = [MyBook::class, MyBestseller::class, MyNytList::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myBookDao(): MyBookDao
    abstract fun myBestsellerDao(): MyBestsellerDao
    abstract fun myNytListDao(): MyNytListDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}
