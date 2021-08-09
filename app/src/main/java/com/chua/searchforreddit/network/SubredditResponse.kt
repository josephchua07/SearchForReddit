package com.chua.searchforreddit.network

data class SubredditResponse(
    val children: List<Wrapper<PostDto>>
)
