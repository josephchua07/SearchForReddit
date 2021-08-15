package com.chua.searchforreddit.ui.search

sealed class SearchEvent {

    object ExecuteSearch : SearchEvent()

    data class SuggestionSelected(val suggestion: String, val scrollState: Int) : SearchEvent()

    data class QueryChange(val query: String) : SearchEvent()

}
