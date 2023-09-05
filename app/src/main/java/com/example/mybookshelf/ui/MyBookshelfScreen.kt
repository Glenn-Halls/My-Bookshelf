package com.example.mybookshelf.ui


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.mybookshelf.R
import com.example.mybookshelf.ui.util.BookshelfNavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookshelfScreen(
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    // Get window width from window size
    val windowWidth = windowSize.widthSizeClass
    // Get window height from window size
    val windowHeight = windowSize.heightSizeClass
    // Define navigation type based on WindowSize dimensions
    val navigationType: BookshelfNavigationType =
        when (windowWidth) {
            WindowWidthSizeClass.Compact -> BookshelfNavigationType.BOTTOM_BAR
            WindowWidthSizeClass.Medium -> BookshelfNavigationType.NAVIGATION_RAIL
            WindowWidthSizeClass.Expanded -> BookshelfNavigationType.NAVIGATION_DRAWER
            else -> BookshelfNavigationType.BOTTOM_BAR
        }
    Scaffold(
        topBar = {
            if (windowHeight != WindowHeightSizeClass.Compact) {
                MyBookshelfTopBar {}
            }
        },
        bottomBar = {
            if (navigationType == BookshelfNavigationType.BOTTOM_BAR) {
                BookshelfBottomNavBar()
            }
        }
    ){ innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                Text(
                    text = windowSize.heightSizeClass.toString(),
                )
                Text(
                    text = windowSize.widthSizeClass.toString(),
                )
                Text(
                    text = "$navigationType"
                )
                BookshelfBottomNavBar()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookshelfTopBar(onUpButtonClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(onClick = onUpButtonClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button),
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
)
fun MyBookshelfScreenPreview(){
    val windowSize = WindowSizeClass.calculateFromSize(DpSize(450.dp, 800.dp))
    MyBookshelfApplication(windowSize = windowSize)
}
