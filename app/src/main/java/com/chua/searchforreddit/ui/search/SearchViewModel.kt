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

    private val _query: MutableState<String> = mutableStateOf("food")
    val query: State<String> = _query

    var scrollPosition = 0
    var mostComments: Pair<String, Int>? = null
    val listOfSuggestions = listOf(
        "food",
        "dog",
        "wedding",
        "happy",
        "android",
        "avengers",
        "pokemon",
        "etoro",
        "iOS",
        "cryptocurrency",
        "mobile"
    )

    init {
        onTriggerEvent(SearchEvent.ExecuteSearch)
    }

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
        redditRepository.getPosts(_query.value).let { posts ->
            getCounts(posts)
            _status.postValue(Status.Success(data = posts))
        }

    }

    private fun getCounts(posts: List<Post>) {
        mostComments = posts
            .maxByOrNull { it.comments }
            ?.let { it.title to it.comments }
    }

}