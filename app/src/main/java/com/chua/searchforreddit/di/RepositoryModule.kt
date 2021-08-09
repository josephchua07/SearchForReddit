package com.chua.searchforreddit.di

import com.chua.searchforreddit.domain.repository.RedditRepository
import com.chua.searchforreddit.domain.repository.RedditRepositoryImpl
import com.chua.searchforreddit.domain.service.RedditService
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
    fun provideRedditRepository(service: RedditService): RedditRepository =
        RedditRepositoryImpl(service)

}