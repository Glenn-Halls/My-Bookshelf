

package com.example.mybookshelf.ui.test

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.mybookshelf.ui.MyBookshelfApp
import com.example.mybookshelf.ui.theme.MyBookshelfTheme
import org.junit.Rule
import org.junit.Test

// Instances of window size classes representing common device configurations
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private val mobilePhonePortrait = WindowSizeClass.calculateFromSize(DpSize(450.dp, 800.dp))
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private val mobilePhoneLandscape = WindowSizeClass.calculateFromSize(DpSize(800.dp, 450.dp))
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private val foldablePortrait = WindowSizeClass.calculateFromSize(DpSize(700.dp, 900.dp))
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private val foldableLandscape = WindowSizeClass.calculateFromSize(DpSize(900.dp, 700.dp))

class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    // Ensure back button is visible in top app bar for mobile phone in portrait mode
    fun mobilePhonePortrait_topAppBar_backButtonVisible() {
        composeTestRule.setContent {
            MyBookshelfTheme {
                MyBookshelfApp(windowSize = mobilePhonePortrait)
            }
        }
        composeTestRule.onNodeWithContentDescription("back button").assertExists()
    }

    @Test
    // Ensure that the back button is visible on mobile phone in landscape mode (via nav rail), but
    // that the top app bar does NOT exist
    fun mobilePhoneLandscape_navRail_backButtonVisible() {
        composeTestRule.setContent {
            MyBookshelfTheme {
                MyBookshelfApp(windowSize = mobilePhoneLandscape)
            }
        }
        composeTestRule.onNodeWithContentDescription("back button").assertExists()
        composeTestRule.onNodeWithContentDescription("My Bookshelf").assertDoesNotExist()
    }
}
