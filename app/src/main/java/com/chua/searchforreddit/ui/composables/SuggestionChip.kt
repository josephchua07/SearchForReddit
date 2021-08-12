package com.chua.searchforreddit.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SuggestionChip(
    text: String,
    isSelected: Boolean = false,
    onSelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp)
            .toggleable(value = isSelected, onValueChange = {
                onSelected(text)
            }),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.large,
        color = if (isSelected) Color.Gray else MaterialTheme.colors.primarySurface
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
    }
}