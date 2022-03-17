package com.ymo.di

import com.ymo.data.DataRepositoryImpl
import com.ymo.data.DataRepositoryHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepositoryImpl: DataRepositoryImpl): DataRepositoryHelper
}
