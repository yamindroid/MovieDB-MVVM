package com.ymo.di

import com.ymo.data.model.error.ErrorManager
import com.ymo.data.model.error.ErrorUseCase
import com.ymo.data.model.error.mapper.ErrorMapperImpl
import com.ymo.data.model.error.mapper.ErrorMapperHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {
    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorUseCase

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapperImpl: ErrorMapperImpl): ErrorMapperHelper
}
