package com.chua.searchforreddit.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.chua.searchforreddit.R
import com.chua.searchforreddit.domain.Post

@Composable
fun PostCard(post: Post, action: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { action.invoke(post.url.drop(1)) },
        border = BorderStroke(2.dp, MaterialTheme.colors.onPrimary),
        elevation = 8.dp
    ) {
        Column {

            post.imageUrl?.let { imageUrl ->
                Image(
                    painter = rememberImagePainter(
                        data = imageUrl,
                        onExecute = { _, _ -> true },
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.ic_baseline_image_24)
                        }
                    ),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = post.title,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.h5
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "likes: ${post.likes}",
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "comments: ${post.comments}",
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.subtitle1
                )
            }

        }
    }
}