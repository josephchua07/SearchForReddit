package com.chua.searchforreddit.repository

import com.chua.searchforreddit.service.RedditService
import com.chua.searchforreddit.domain.DomainMapper
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.network.PostDto
import javax.inject.Inject

class RedditRepositoryImpl @Inject constructor(
    private val redditService: RedditService,
    private val postMapper: DomainMapper<PostDto, Post>
) : RedditRepository {

    override suspend fun getPosts(subreddit: String): List<Post> =
        postMapper.toListOfDomain(
            redditService.getSubreddit(subreddit).data.children.map { it.data }
        )

}