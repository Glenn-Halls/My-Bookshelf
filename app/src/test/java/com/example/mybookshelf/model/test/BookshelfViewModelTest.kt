package com.example.mybookshelf.model.test

import com.example.mybookshelf.model.BookshelfViewModel
import com.example.mybookshelf.ui.util.NavigationTabs
import com.example.mybookshelf.ui.util.ScreenSelect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BookshelfViewModelTest {
    private val viewModel = BookshelfViewModel()

    @Test
    fun viewModel_initialValue_screenSelectIsnull() {
        val currentScreenSelected = viewModel.uiState.value.currentScreen
        assertTrue(currentScreenSelected == null)
    }

    @Test
    // Get navigation element from navigation tabs and navigate to bestseller screen
    fun viewModel_navigateToScreen_screenSelectedIsBestSellers() {
        val bestSellerScreen = ScreenSelect.BEST_SELLERS
        // Get navigation element from navigation tabs
        val bestSellerNavigationElement = NavigationTabs.first {
            it.screenSelect == bestSellerScreen
        }
        // Navigate to bestseller screen
        viewModel.navigateToScreen(bestSellerNavigationElement)

        val currentSelectedScreen = viewModel.uiState.value.currentScreen

        assertEquals(bestSellerScreen, currentSelectedScreen)
    }

    @Test
    // Get navigation element from navigation tabs and navigate to watch list screen
    fun viewModel_navigateToScreen_screenSelectedIsWatchList() {
        val watchListScreen = ScreenSelect.WATCH_LIST
        // Get navigation element from navigation tabs
        val bestSellerNavigationElement = NavigationTabs.first {
            it.screenSelect == watchListScreen
        }
        // Navigate to watch list screen
        viewModel.navigateToScreen(bestSellerNavigationElement)

        val currentSelectedScreen = viewModel.uiState.value.currentScreen

        assertEquals(watchListScreen, currentSelectedScreen)
    }
    @Test
    // Get navigation element from navigation tabs and navigate to browse screen
    fun viewModel_navigateToScreen_screenSelectedIsBrowse() {
        val browseScreen = ScreenSelect.BROWSE
        // Get navigation element from navigation tabs
        val bestSellerNavigationElement = NavigationTabs.first {
            it.screenSelect == browseScreen
        }
        // Navigate to browse screen
        viewModel.navigateToScreen(bestSellerNavigationElement)

        val currentSelectedScreen = viewModel.uiState.value.currentScreen

        assertEquals(browseScreen, currentSelectedScreen)
    }

    @Test
    // Get navigation element from navigation tabs and navigate to my books screen
    fun viewModel_navigateToScreen_screenSelectedIsMyBooks() {
        val myBooksScreen = ScreenSelect.MY_BOOKS
        // Get navigation element from navigation tabs
        val bestSellerNavigationElement = NavigationTabs.first {
            it.screenSelect == myBooksScreen
        }
        // Navigate to browse screen
        viewModel.navigateToScreen(bestSellerNavigationElement)

        val currentSelectedScreen = viewModel.uiState.value.currentScreen

        assertEquals(myBooksScreen, currentSelectedScreen)
    }

    @Test
    // Get navigation element from navigation tabs and navigate to my favourites
    fun viewModel_navigateToScreen_screenSelectedIsFavourites() {
        val favouritesScreen = ScreenSelect.FAVOURITES
        // Get navigation element from navigation tabs
        val bestSellerNavigationElement = NavigationTabs.first {
            it.screenSelect == favouritesScreen
        }
        // Navigate to browse screen
        viewModel.navigateToScreen(bestSellerNavigationElement)

        val currentSelectedScreen = viewModel.uiState.value.currentScreen

        assertEquals(favouritesScreen, currentSelectedScreen)
    }
}