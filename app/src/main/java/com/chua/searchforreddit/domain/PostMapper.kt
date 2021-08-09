package com.chua.searchforreddit.domain

import com.chua.searchforreddit.network.PostDto

class PostMapper : DomainMapper<PostDto, Post> {

    override fun toDomain(data: PostDto) = Post(
        data.title,
        data.ups,
        data.num_comments,
        data.permalink
    )

    override fun toListOfDomain(dataList: List<PostDto>) =
        dataList.map { toDomain(it) }
}