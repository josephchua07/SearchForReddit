package com.chua.searchforreddit.di

import com.chua.searchforreddit.domain.DomainMapper
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.network.PostDto
import com.chua.searchforreddit.repository.RedditRepository
import com.chua.searchforreddit.repository.RedditRepositoryImpl
import com.chua.searchforreddit.service.RedditService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRedditRepository(
        service: RedditService,
        postMapper: DomainMapper<PostDto, Post>
    ): RedditRepository =
        RedditRepositoryImpl(service, postMapper)

}