package com.example.mybookshelf.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mybookshelf.R

data class NavigationElement(
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int,
    val screenSelect: ScreenSelect?,
    @StringRes
    val description: Int? = null
)

// Navigation elements to be used by bottom nav, navigation rail, nav drawer and home screen
val NavigationTabs = listOf<NavigationElement>(
    NavigationElement(
        R.string.best_sellers,
        R.drawable.bestseller,
        ScreenSelect.BEST_SELLERS,
        R.string.bestseller_description,
    ),
    NavigationElement(
        R.string.watch_list,
        R.drawable.watch_list,
        ScreenSelect.WATCH_LIST,
        R.string.watch_screen_description,
    ),
    NavigationElement(
        R.string.browse,
        R.drawable.search,
        ScreenSelect.BROWSE,
        R.string.browse_description
    ),
    NavigationElement(
        R.string.my_books,
        R.drawable.owned,
        ScreenSelect.MY_BOOKS,
        R.string.my_books_description,
    ),
    NavigationElement(
        R.string.favourites,
        R.drawable.favourite,
        ScreenSelect.FAVOURITES,
        R.string.favourites_description,
    )
)

val NavigateBackTab = NavigationElement(R.string.back_button, R.drawable.back_arrow, ScreenSelect.SETTINGS)


