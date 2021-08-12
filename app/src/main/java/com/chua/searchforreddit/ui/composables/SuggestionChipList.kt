package com.chua.searchforreddit.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SuggestionChipList(
    suggestions: List<String>,
    selectedSuggestion: String?,
    preservedScroll: Int,
    scrollState: ScrollState,
    scope: CoroutineScope,
    onItemSelected: (String, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .horizontalScroll(state = scrollState)
    ) {

        scope.launch { scrollState.scrollTo(preservedScroll) }

        suggestions.forEach { suggestion ->
            SuggestionChip(
                text = suggestion,
                isSelected = selectedSuggestion == suggestion
            ) { onItemSelected(it, scrollState.value) }
        }
    }
}