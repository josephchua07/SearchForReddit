package com.chua.searchforreddit.domain

data class Post(
    val title: String,
    val likes: Int,
    val comments: Int,
    val url: String,
    val imageUrl: String?
)