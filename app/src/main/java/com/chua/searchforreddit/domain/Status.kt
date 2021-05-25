package com.chua.searchforreddit.domain

import com.chua.searchforreddit.domain.model.Post
import java.lang.Exception

sealed class Status {
    data class Success(val data: List<Post>) : Status()
    data class Error(val e: Exception) : Status()
    object Loading : Status()
}