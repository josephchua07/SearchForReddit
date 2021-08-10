package com.chua.searchforreddit.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.network.Status
import com.chua.searchforreddit.repository.RedditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val redditRepository: RedditRepository
) : ViewModel() {

    private val _status = MutableLiveData<Status<Post>>()
    val status: LiveData<Status<Post>>
        get() = _status

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _searchText = MutableLiveData("avengers/hot")
    val searchText: LiveData<String> = _searchText

    fun setSearchText(text: String) {
        _searchText.value = text
    }

    fun getSubreddit(subreddit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _status.postValue(Status.Loading)
                redditRepository.getPosts(subreddit).let {
                    _posts.postValue(it)
                    _status.postValue(Status.Success(it))
                }
            } catch (e: Exception) {
                _status.postValue(Status.Error(e))
            }
        }
    }

    fun findNoUpVotes() = getSizeWhere { it.likes == 0 }

    fun findFivePlusUpVotes() = getSizeWhere { it.likes > 5 }

    fun findNoComments() = getSizeWhere { it.comments == 0 }

    fun findFivePlusComments() = getSizeWhere { it.comments > 5 }

    fun findMostComments() = _posts.value
        ?.maxByOrNull { it.comments }
        ?.let { it.title to it.comments }

    private fun getSizeWhere(action: (post: Post) -> Boolean) =
        _posts.value?.filter { action(it) }?.size ?: 0

}