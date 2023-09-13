package com.example.mybookshelf.model

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookshelf.MyBookshelfApplication
import com.example.mybookshelf.data.BestsellerRepository
import com.example.mybookshelf.data.BookRepository
import com.example.mybookshelf.ui.util.BookshelfNavigationType
import com.example.mybookshelf.ui.util.NavigationElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookshelfViewModel(
    private val bookRepository: BookRepository,
    private val bestsellerRepository: BestsellerRepository,
) : ViewModel() {

    // Create observable state holder
    private val _uiState = MutableStateFlow(BookshelfUiState())
    // Accessor to state values
    val uiState: StateFlow<BookshelfUiState> = _uiState

    // Navigate to selected screen
    fun navigateToScreen(nav: NavigationElement) {
        _uiState.update {
            it.copy(
                currentScreen = nav.screenSelect
            )
        }
    }

    fun searchBooks() {
        viewModelScope.launch {
            var searchResult: SearchResult
            try {
                searchResult = bookRepository.getBooks()
            } catch (e: Exception) {
                Log.e("ViewModel", e.message.toString())
                searchResult = SearchResult(0, emptyList())
            }
            _uiState.update {
                it.copy(
                    searchResult = searchResult
                )
            }
        }
    }

    fun getBestsellers() {
        viewModelScope.launch {
            val result: String = try {
                val jsonReply = bestsellerRepository.getBestsellers()
                "Success, ${jsonReply.totalItems} books found."
            } catch (e: Exception) {
                "Bestseller retrieval failure: ${e.message}"
            }
            _uiState.update {
                it.copy(
                    bestSellers = result
                )
            }
        }
    }

    // Get navigation setup based on window size
    fun getNavigationSetup(windowSize: WindowSizeClass): BookshelfNavigationType {
        return when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> BookshelfNavigationType.BOTTOM_BAR
            WindowWidthSizeClass.Medium -> BookshelfNavigationType.NAVIGATION_RAIL
            WindowWidthSizeClass.Expanded -> BookshelfNavigationType.NAVIGATION_DRAWER
            else -> BookshelfNavigationType.BOTTOM_BAR
        }
    }

    init {
        searchBooks()
        getBestsellers()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MyBookshelfApplication)
                val bookRepository = application.container.bookRepository
                val bestsellerRepository = application.container.bestsellerRepository
                BookshelfViewModel(bookRepository, bestsellerRepository)
            }
        }
    }
}
