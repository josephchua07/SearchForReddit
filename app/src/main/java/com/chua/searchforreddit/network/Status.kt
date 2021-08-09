package com.chua.searchforreddit.network

import java.lang.Exception

sealed class Status <out T>{
    data class Success <out T>(val data: List<T>) : Status<T>()
    data class Error(val e: Exception) : Status<Nothing>()
    object Loading : Status<Nothing>()
}