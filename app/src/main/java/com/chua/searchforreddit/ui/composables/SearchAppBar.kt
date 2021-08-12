package com.chua.searchforreddit.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    scrollPosition: Int,
    suggestions: List<String>,
    onSuggestionSelected: (String, Int) -> Unit,
    onToggleTheme: () -> Unit
) {

    Surface(elevation = 8.dp) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchTextField(
                    label = "subreddits/communities",
                    value = query,
                    modifier = Modifier.fillMaxWidth(0.85f),
                    onValueChange = { onQueryChange(it) },
                    onKeyboardSearchClicked = onExecuteSearch
                )

                IconButton(onClick = onToggleTheme) {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
                }
            }

            SuggestionChipList(
                suggestions = suggestions,
                selectedSuggestion = query,
                preservedScroll = scrollPosition
            ) { selectedSuggestion, scrollState ->
                onSuggestionSelected(selectedSuggestion, scrollState)
            }
        }
    }
}