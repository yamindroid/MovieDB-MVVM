package com.ymo.di

import android.content.Context
import androidx.room.Room
import com.ymo.data.local.db.AppDatabase
import com.ymo.data.local.db.DBHelper
import com.ymo.data.local.db.DBHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): DBHelper {
        val appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "movie-db")
            .build()
        return DBHelperImpl(appDatabase)
    }

}
