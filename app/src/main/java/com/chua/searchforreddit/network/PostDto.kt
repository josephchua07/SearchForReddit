package com.chua.searchforreddit.network

data class PostDto(
    val title: String,
    val ups: Int,
    val num_comments: Int,
    val permalink: String,
    val thumbnail: String
)
