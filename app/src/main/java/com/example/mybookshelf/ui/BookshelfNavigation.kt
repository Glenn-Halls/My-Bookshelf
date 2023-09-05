package com.example.mybookshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.mybookshelf.R

import com.example.mybookshelf.ui.util.NavigationTabs

@Composable
fun BookshelfBottomNavBar(
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .height(dimensionResource(R.dimen.bottom_nav_bar_height))
    ) {
        NavigationTabs.forEach {
            NavigationBarItem(
                selected = false,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = stringResource(id = it.name),
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_medium))
                            .size(40.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.onSecondary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}
