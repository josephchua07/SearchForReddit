package com.chua.searchforreddit.network

sealed class Status<out T> {
    data class Success<out T>(
        val data: List<T>,
        val noUpVotesCount: Int,
        val fivePlusUpVotesCount: Int,
        val noCommentsCount: Int,
        val fivePlusCommentsCount: Int,
        val mostComments: Pair<String, Int>?
    ) : Status<T>()
    data class Error(val e: Exception) : Status<Nothing>()
    object Loading : Status<Nothing>()
}