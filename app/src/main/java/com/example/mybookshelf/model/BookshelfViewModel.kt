package com.example.mybookshelf.model

import androidx.lifecycle.ViewModel
import com.example.mybookshelf.ui.util.ScreenSelect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BookshelfViewModel : ViewModel() {

    // Create observable state holder
    private val _uiState = MutableStateFlow(BookshelfUiState())
    // Accessor to state values
    val uiState: StateFlow<BookshelfUiState> = _uiState

    fun navigateToScreen(screen: ScreenSelect) {
        _uiState.update {
            it.copy(
                currentScreen = screen
            )
        }
    }
}
