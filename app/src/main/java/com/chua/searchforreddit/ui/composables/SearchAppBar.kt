package com.chua.searchforreddit.ui.composables

import androidx.compose.foundation.ScrollState
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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    scrollPosition: Int,
    suggestions: List<String>,
    suggestionsScrollState: ScrollState,
    coroutineScope: CoroutineScope,
    onSuggestionSelected: (String, Int) -> Unit,
    onToggleTheme: () -> Unit
) {

    Surface(
        elevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchTextField(
                    label = "subreddits/communities",
                    value = query,
                    modifier = Modifier.fillMaxWidth(0.90f),
                    onValueChange = { onQueryChange(it) },
                    keyboardController = keyboardController,
                    onKeyboardSearchClicked = onExecuteSearch
                )

                IconButton(onClick = onToggleTheme) {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
                }
            }

            SuggestionChipList(
                suggestions = suggestions,
                selectedSuggestion = query,
                preservedScroll = scrollPosition,
                scrollState = suggestionsScrollState,
                scope = coroutineScope
            ) { selectedSuggestion, scrollState ->
                onSuggestionSelected(selectedSuggestion, scrollState)
            }
        }
    }
}