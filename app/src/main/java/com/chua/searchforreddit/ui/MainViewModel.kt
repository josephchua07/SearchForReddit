package com.chua.searchforreddit.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chua.searchforreddit.domain.model.Post
import com.chua.searchforreddit.domain.repository.RedditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val redditRepository: RedditRepository
): ViewModel() {

    val posts: MutableLiveData<List<Post>> by lazy { MutableLiveData<List<Post>>() }

    fun getSubreddit(subreddit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.postValue(redditRepository.getPosts(subreddit))
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