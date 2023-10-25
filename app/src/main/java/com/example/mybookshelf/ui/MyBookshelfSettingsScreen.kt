package com.example.mybookshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybookshelf.ProtoData.DarkMode
import com.example.mybookshelf.R
import com.example.mybookshelf.model.SortOrder
import com.example.mybookshelf.ui.theme.MyBookshelfTheme
import com.example.mybookshelf.ui.util.ActionButton
import com.example.mybookshelf.ui.util.NavigationElement
import com.example.mybookshelf.ui.util.NavigationTabs
import com.example.mybookshelf.ui.util.ScreenSelect
import com.example.mybookshelf.ui.util.SortOrderActionButtonList

fun SortOrder.getShortDescription(): String {
    return when (this) {
        SortOrder.ALPHABETICAL -> "alphabetical"
        SortOrder.ALPHABETICAL_REVERSE -> "reverse-alphabetical"
        SortOrder.LAST_UPDATED -> "last updated"
        SortOrder.LAST_UPDATED_REVERSE -> "first updated"
        SortOrder.LAST_ADDED -> "last added"
        SortOrder.LAST_ADDED_REVERSE -> "first added"
    }
}

@Composable
fun SettingsScreen(
    darkMode: Boolean?,
    onDarkModeClick: (DarkMode) -> Unit,
    onStartScreenClick: (NavigationElement) -> Unit,
    sortOrderOptions: List<Pair<SortOrder, ActionButton>>,
    onSortOrderClick: (SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    val darkModeString = when (darkMode) {
        true -> "Dark"
        false -> "Light"
        null -> "Match Phone"
    }
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(.15f)
        ) {
            Text(
                text = "Settings",
                fontSize = 40.sp,
                style = MaterialTheme.typography.displayMedium,
            )
        }
        Text(
            text = "Dark Mode: $darkModeString",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DarkModeButton(
                darkMode = DarkMode.DARK,
                isActive = darkMode ?: false,
                icon = Icons.Filled.DarkMode,
                contentDescription = "dark mode",
                onDarkModeClick = onDarkModeClick
            )
            DarkModeButton(
                darkMode = DarkMode.LIGHT,
                isActive = !(darkMode ?: true),
                icon = Icons.Filled.LightMode,
                contentDescription = "light mode",
                onDarkModeClick = onDarkModeClick
            )
            DarkModeButton(
                darkMode = DarkMode.PHONE,
                isActive = darkMode == null,
                icon = Icons.Filled.PhoneAndroid,
                contentDescription = "match phone",
                onDarkModeClick = onDarkModeClick
            )
        }
        Text(
            text = "Startup Screen: ",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            NavigationTabs.forEach {
                StartupScreenButton(
                    navigationElement = it,
                    isActive = false,
                    drawable = painterResource(it.icon),
                    contentDescription = it.name.toString(),
                    onStartScreenClick = onStartScreenClick,
                )
            }
            StartupScreenButton(
                navigationElement = NavigationElement(
                    name = R.string.app_name,
                    icon = R.drawable.ic_broken_image,
                    screenSelect = ScreenSelect.NONE
                ),
                isActive = false,
                icon = Icons.Filled.Home,
                contentDescription = "home",
                onStartScreenClick = onStartScreenClick,
            )
        }
        Text(
            text = "Sort Order: ",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            sortOrderOptions.forEach {
                SortOrderButton(
                    sortOrder = it.first,
                    actionButton = it.second,
                    isActive = false,
                    contentDescription = it.second.contentDescription ?: "unknown",
                    onSortOrderClick = onSortOrderClick
                )
            }
        }
    }
}

@Composable
fun DarkModeButton(
    darkMode: DarkMode,
    isActive: Boolean,
    contentDescription: String,
    onDarkModeClick: (DarkMode) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector
) {
    val buttonSize = if (isActive) 70.dp else 55.dp
    IconButton(
        onClick = { onDarkModeClick(darkMode) },
        modifier = modifier
            .size(buttonSize)
            .padding(PaddingValues(dimensionResource(R.dimen.padding_medium)))
    ) {
        Image(
            imageVector = icon,
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(
                if (isActive) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun StartupScreenButton(
    navigationElement: NavigationElement,
    isActive: Boolean,
    contentDescription: String,
    onStartScreenClick: (NavigationElement) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    drawable: Painter? = null,
) {
    val buttonSize = if (isActive) 70.dp else 55.dp
    IconButton(
        onClick = { onStartScreenClick(navigationElement) },
        modifier = modifier
            .size(buttonSize)
            .padding(PaddingValues(dimensionResource(R.dimen.padding_medium)))
    ) {
        if (icon != null) {
            Image(
                imageVector = icon,
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(
                    if (isActive) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                ),
                modifier = Modifier.fillMaxSize()
            )
        } else if (drawable != null) {
            Image(
                painter = drawable,
                contentDescription = contentDescription,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(
                    if (isActive) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                ),
                modifier = Modifier.size(buttonSize.minus(26.dp))
            )
        } else {
            Image(
                imageVector = Icons.Filled.Error,
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(
                    if (isActive) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                ),
                modifier = Modifier.size(buttonSize)
            )
        }
    }
}

@Composable
fun SortOrderButton(
    actionButton: ActionButton,
    sortOrder: SortOrder,
    isActive: Boolean,
    contentDescription: String,
    onSortOrderClick: (SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    val xOffsetValue = if (actionButton.mirrorIcon == true) -1f else 1f
    val buttonSize = if (isActive) 70.dp else 55.dp
    IconButton(
        onClick = { onSortOrderClick(sortOrder) },
        modifier = modifier
            .size(buttonSize)
            .padding(PaddingValues(dimensionResource(R.dimen.padding_medium)))
    ) {
        Image(
            imageVector = actionButton.icon ?: Icons.Default.Error,
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(
                if (isActive) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            ),
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleX = xOffsetValue, scaleY = 1f)
        )
    }
}



@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SettingsScreenPreview() {
    MyBookshelfTheme {
        SettingsScreen(
            darkMode = true,
            onDarkModeClick = {},
            onStartScreenClick = {},
            sortOrderOptions = SortOrderActionButtonList,
            onSortOrderClick = {},
        )
    }
}
