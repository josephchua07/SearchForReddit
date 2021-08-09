package com.chua.searchforreddit.repository

import com.chua.searchforreddit.domain.Post

interface RedditRepository {
    suspend fun getPosts(subreddit: String): List<Post>
}