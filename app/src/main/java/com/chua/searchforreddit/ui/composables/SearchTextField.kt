package com.chua.searchforreddit.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun SearchTextField(
    label: String,
    value: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onKeyboardSearchClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .padding(4.dp),
        value = value,
        onValueChange = { onValueChange.invoke(it) },
        label = { Text(text = label) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search"
            )
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                onKeyboardSearchClicked.invoke()
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        )
    )
}