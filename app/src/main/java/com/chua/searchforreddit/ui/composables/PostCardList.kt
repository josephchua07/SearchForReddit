package com.chua.searchforreddit.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chua.searchforreddit.domain.Post

@Composable
fun PostCardList(
    postList: List<Post>,
    action: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        items(postList.size) { index ->
            PostCard(post = postList[index]) { url ->
                action.invoke(url)
            }
        }
    }
}