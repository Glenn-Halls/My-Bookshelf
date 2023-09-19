package com.example.mybookshelf.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.Bestseller

@Composable
fun NytBestsellerScreen(
    bestsellers: List<Bestseller>,
    onCardClick: (Bestseller) -> Unit,
    nytListSelected: String,
    showSearch: Boolean,
    modifier: Modifier = Modifier,
) {
    if (nytListSelected != null) {
        BestsellerGrid(bestsellers = bestsellers, onCardClick = onCardClick)
    }
    else {
        Text(nytListSelected ?: "null")
        Text(showSearch.toString())
    }
}
