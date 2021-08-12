package com.chua.searchforreddit.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SuggestionChipList(
    suggestions: List<String>,
    selectedSuggestion: String?,
    preservedScroll: Int,
    onItemSelected: (String, Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
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