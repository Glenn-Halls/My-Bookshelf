package com.example.mybookshelf.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mybookshelf.R

data class NavigationElement(
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int,
)

val NavigationTabs = listOf<NavigationElement>(
    NavigationElement(R.string.best_sellers, R.drawable.bestseller),
    NavigationElement(R.string.watch_list, R.drawable.watch_list),
    NavigationElement(R.string.browse, R.drawable.search),
    NavigationElement(R.string.my_books, R.drawable.owned),
    NavigationElement(R.string.favourites, R.drawable.favourite)
)


