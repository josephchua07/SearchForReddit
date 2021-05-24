package com.chua.searchforreddit.domain.model

data class Post(
    val title: String,
    val ups: Int,
    val num_comments: Int,
    val permalink: String
)
