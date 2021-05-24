package com.chua.searchforreddit.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chua.searchforreddit.domain.model.Post
import com.chua.searchforreddit.domain.service.RedditService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(): ViewModel() {

    private val redditService: RedditService

    val posts: MutableLiveData<List<Post>> by lazy { MutableLiveData<List<Post>>() }

    init {
        //TODO: create DI

        val logging = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        redditService = retrofit.create(RedditService::class.java)
    }

    fun getSubreddit(subreddit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.postValue(redditService.getSubreddit(subreddit).data.children.map { it.data })
        }
    }

    fun findNoUpVotes() =
        "${getSizeWhere { it.ups == 0 }} posts have no upvotes"

    fun findFivePlusUpVotes() =
        "Great, ${getSizeWhere { it.ups > 5 }} post/s has more than 5 upvotes"

    fun findNoComments() =
        "${getSizeWhere { it.num_comments == 0 }} posts have no comments"

    fun findFivePlusComments() =
        "Great, ${getSizeWhere { it.num_comments > 5 }} post/s has more than 5 comments"

    fun findMostComments(): String? {
        return posts.value
            ?.maxByOrNull { it.num_comments }
            ?.let { it.title to it.num_comments }
            ?.let { "Great, ${it.first} has the most comments with ${it.second}" }
    }

    private fun getSizeWhere(action: (post: Post) -> Boolean) =
        posts.value?.filter { action(it) }?.size ?: 0
}