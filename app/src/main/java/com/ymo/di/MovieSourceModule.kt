package com.ymo.di

import com.ymo.data.DataRepositoryHelper
import com.ymo.data.paging.NowPlayingPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieSourceModule {

    @Provides
    @Singleton
     fun provideMoviesPagingSource(dataRepositoryHelper: DataRepositoryHelper): NowPlayingPagingSource {
        return NowPlayingPagingSource(dataRepositoryHelper)
    }

}