package com.chua.searchforreddit.domain.service

import com.chua.searchforreddit.domain.model.Response
import com.chua.searchforreddit.domain.model.Subreddit
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://www.reddit.com/"

interface RedditService {

    @GET("$BASE_URL/r/{subreddit}.json")
    suspend fun getSubreddit(@Path("subreddit") subreddit: String) : Response<Subreddit>

}