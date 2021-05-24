package com.chua.searchforreddit.domain.model

data class Subreddit(
    val children: List<Response<Post>>
)
