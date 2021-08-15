package com.chua.searchforreddit.ui.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.network.Status
import com.chua.searchforreddit.repository.RedditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _query: MutableState<String> = mutableStateOf("avengers/hot")
    val query: State<String> = _query

    var scrollPosition = 0

    var noUpVotesCount = 0
    var fivePlusUpVotesCount = 0
    var noCommentsCount = 0
    var fivePlusCommentsCount = 0
    var mostComments: Pair<String, Int>? = null

    val listOfSuggestions = listOf(
        "food",
        "dog",
        "wedding",
        "happy",
        "android",
        "pokemon",
        "etoro",
        "iOS",
        "cryptocurrency",
        "mobile"
    )

    fun onTriggerEvent(event: SearchEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is SearchEvent.ExecuteSearch -> {
                        executeSearch()
                    }
                    is SearchEvent.SuggestionSelected -> {
                        _query.value = event.suggestion
                        scrollPosition = event.scrollState
                        executeSearch()
                    }
                    is SearchEvent.QueryChange -> {
                        _query.value = event.query
                    }
                }
            } catch (e: Exception) {
                _status.postValue(Status.Error(e))
            }
        }
    }

    private suspend fun executeSearch() {
        _status.postValue(Status.Loading)
        redditRepository.getPosts(_query.value).let {
            _posts.value = it
            getCounts()
            _status.postValue(
                Status.Success(
                    data = it,
                    noUpVotesCount = noUpVotesCount,
                    fivePlusUpVotesCount = fivePlusUpVotesCount,
                    noCommentsCount = noCommentsCount,
                    fivePlusCommentsCount = fivePlusCommentsCount,
                    mostComments = mostComments
                )
            )
        }

    }

    private fun getCounts() {
        noUpVotesCount = getSizeWhere { it.likes == 0 }
        fivePlusUpVotesCount = getSizeWhere { it.likes > 5 }
        noCommentsCount = getSizeWhere { it.comments == 0 }
        fivePlusCommentsCount = getSizeWhere { it.comments > 5 }
        mostComments = _posts.value
            ?.maxByOrNull { it.comments }
            ?.let { it.title to it.comments }
    }

    private fun getSizeWhere(action: (post: Post) -> Boolean) =
        _posts.value?.filter { action(it) }?.size ?: 0

}