package com.chua.searchforreddit.domain

data class Post(
    val title: String,
    val ups: Int,
    val numComments: Int,
    val permalink: String
)