package com.example.mybookshelf.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mybookshelf.R

data class NavigationElement(
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int,
    val screenSelect: ScreenSelect
)

// Navigation elements to be used by bottom nav, navigation rail and nav drawer
val NavigationTabs = listOf<NavigationElement>(
    NavigationElement(R.string.best_sellers, R.drawable.bestseller, ScreenSelect.BEST_SELLERS),
    NavigationElement(R.string.watch_list, R.drawable.watch_list, ScreenSelect.WATCH_LIST),
    NavigationElement(R.string.browse, R.drawable.search, ScreenSelect.BROWSE),
    NavigationElement(R.string.my_books, R.drawable.owned, ScreenSelect.MY_BOOKS),
    NavigationElement(R.string.favourites, R.drawable.favourite, ScreenSelect.FAVOURITES)
)

val NavigateBackTab = NavigationElement(R.string.back_button, R.drawable.back_arrow, ScreenSelect.NONE)


