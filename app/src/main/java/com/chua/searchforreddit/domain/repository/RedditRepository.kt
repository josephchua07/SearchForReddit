package com.chua.searchforreddit.domain.repository

import com.chua.searchforreddit.domain.model.Post

interface RedditRepository {
    suspend fun getPosts(subreddit: String): List<Post>
}