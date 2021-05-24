package com.chua.searchforreddit.domain.model

data class Wrapper<T>(
    val kind: String,
    val data: T
)
