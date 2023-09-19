package com.example.mybookshelf.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.Bestseller

@Composable
fun NytBestsellerScreen(
    bestsellers: List<Bestseller>,
    onCardClick: (Bestseller) -> Unit,
    modifier: Modifier = Modifier,
) {
    BestsellerGrid(bestsellers = bestsellers, onCardClick = onCardClick)
}
