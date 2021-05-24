package com.chua.searchforreddit.domain.service

import com.chua.searchforreddit.domain.model.Response
import com.chua.searchforreddit.domain.model.Subreddit
import retrofit2.http.GET
import retrofit2.http.Path

interface RedditService {

    @GET("/r/{subreddit}.json")
    suspend fun getSubreddit(@Path("subreddit") subreddit: String) : Response<Subreddit>

}