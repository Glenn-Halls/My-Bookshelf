package com.example.mybookshelf.model.test

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.mybookshelf.fake.FakeMyBookRepository
import com.example.mybookshelf.fake.FakeNetworkBestsellerRepository
import com.example.mybookshelf.fake.FakeNetworkBookRepository
import com.example.mybookshelf.fake.FakeNytListRepository
import com.example.mybookshelf.model.BookshelfViewModel
import com.example.mybookshelf.rules.TestDispatcherRule
import com.example.mybookshelf.ui.util.BookshelfNavigationType
import com.example.mybookshelf.ui.util.NavigationTabs
import com.example.mybookshelf.ui.util.ScreenSelect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class BookshelfViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()
    // Instance of viewModel to be used for testing
    private lateinit var viewModel: BookshelfViewModel
    private val newViewModel = { BookshelfViewModel(
        bookRepository = FakeNetworkBookRepository(),
        bestsellerRepository = FakeNetworkBestsellerRepository(),
        nytListRepository = FakeNytListRepository(),
        myBookRepository = FakeMyBookRepository(),
    ) }

    // Instances of window size classes representing common device configurations
    private val mobilePhonePortrait = WindowSizeClass.calculateFromSize(DpSize(450.dp, 800.dp))
    private val mobilePhoneLandscape = WindowSizeClass.calculateFromSize(DpSize(800.dp, 450.dp))
    private val foldablePortrait = WindowSizeClass.calculateFromSize(DpSize(700.dp, 900.dp))
    private val foldableLandscape = WindowSizeClass.calculateFromSize(DpSize(900.dp, 700.dp))

    @Test
    fun viewModel_initialValue_screenSelectIsnull() =
        runTest {
            viewModel = newViewModel()
            val currentScreenSelected = viewModel.uiState.value.currentScreen
            assertTrue(currentScreenSelected == null)
        }

    @Test
    // Get navigation element from navigation tabs and navigate to bestseller screen
    fun viewModel_navigateToScreen_screenSelectedIsBestSellers() {
        viewModel = newViewModel()
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
        viewModel = newViewModel()
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
        viewModel = newViewModel()
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
        viewModel = newViewModel()
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
        viewModel = newViewModel()
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



    @Test
    // Get Bottom Bar navigation for portrait mobile phone
    fun viewModel_getNavigationSetup_BottomBarNavigation() {
        viewModel = newViewModel()
        val bottomBarNavigation = BookshelfNavigationType.BOTTOM_BAR
        val currentNavigation = viewModel.getNavigationSetup(mobilePhonePortrait)
        assertEquals(bottomBarNavigation, currentNavigation)
    }

    @Test
    // Get Navigation Rail for landscape mobile phone
    fun viewModel_getNavigationSetup_NavigationRail() {
        viewModel = newViewModel()
        val navigationRail = BookshelfNavigationType.NAVIGATION_RAIL
        val currentNavigation = viewModel.getNavigationSetup(mobilePhoneLandscape)
        assertEquals(navigationRail, currentNavigation)
    }

    @Test
    // Get Navigation Drawer for landscape foldable
    fun viewModel_getNavigationSetup_NavigationDrawer() {
        viewModel = newViewModel()
        val navigationDrawer = BookshelfNavigationType.NAVIGATION_DRAWER
        val currentNavigation = viewModel.getNavigationSetup(foldableLandscape)
        assertEquals(navigationDrawer, currentNavigation)
    }

    @Test
    // Get Navigation Rail for portrait foldable
    fun viewModel_getNavigationSetup_FoldablePortrait() {
        viewModel = newViewModel()
        val navigationRail = BookshelfNavigationType.NAVIGATION_RAIL
        val currentNavigation = viewModel.getNavigationSetup(foldablePortrait)
        assertEquals(navigationRail, currentNavigation)
    }
}
