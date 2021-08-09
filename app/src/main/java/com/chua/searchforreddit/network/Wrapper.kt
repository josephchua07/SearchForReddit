package com.chua.searchforreddit.network

data class Wrapper<T>(
    val kind: String,
    val data: T
)
