package com.example.mybookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mybookshelf.ui.MyBookshelfScreen
import com.example.mybookshelf.ui.theme.MyBookshelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBookshelfTheme {
                MyBookshelfScreen()
            }
        }
    }
}
