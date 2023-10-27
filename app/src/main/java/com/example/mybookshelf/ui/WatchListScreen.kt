package com.example.mybookshelf.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.MyBestseller

@Composable
fun WatchScreen(
    myBestsellerList: List<MyBestseller>,
    onMyBestsellerClick: (MyBestseller) -> Unit,
    onStarClick: (MyBestseller) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text("Test")
}
