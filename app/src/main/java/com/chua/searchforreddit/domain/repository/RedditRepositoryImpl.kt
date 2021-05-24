package com.chua.searchforreddit.domain.repository

import com.chua.searchforreddit.domain.model.Post
import com.chua.searchforreddit.domain.service.RedditService
import javax.inject.Inject

class RedditRepositoryImpl @Inject constructor(
    private val redditService: RedditService
) : RedditRepository {

    override suspend fun getPosts(subreddit: String): List<Post> =
        redditService.getSubreddit(subreddit).data.children.map { it.data }

}