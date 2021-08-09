package com.chua.searchforreddit.service

import com.chua.searchforreddit.network.SubredditResponse
import com.chua.searchforreddit.network.Wrapper
import retrofit2.http.GET
import retrofit2.http.Path

interface RedditService {

    @GET("/r/{subreddit}.json")
    suspend fun getSubreddit(@Path("subreddit") subreddit: String): Wrapper<SubredditResponse>

}