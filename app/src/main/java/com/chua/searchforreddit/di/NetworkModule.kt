package com.chua.searchforreddit.di

import com.chua.searchforreddit.BuildConfig
import com.chua.searchforreddit.domain.DomainMapper
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.domain.PostMapper
import com.chua.searchforreddit.network.PostDto
import com.chua.searchforreddit.service.RedditService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRedditService(): RedditService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(RedditService::class.java)

    @Provides
    @Singleton
    fun providePostMapper(): DomainMapper<PostDto, Post> = PostMapper()

}