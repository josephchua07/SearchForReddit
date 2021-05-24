package com.chua.searchforreddit.domain.model

data class Response<T>(
    val kind: String,
    val data: T
)
