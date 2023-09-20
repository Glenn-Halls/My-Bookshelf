package com.example.mybookshelf

import android.app.Application
import com.example.mybookshelf.data.AppContainer
import com.example.mybookshelf.data.DefaultAppContainer

class MyBookshelfApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
