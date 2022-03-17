package com.ymo.di

import android.content.Context
import android.content.SharedPreferences
import com.ymo.BuildConfig
import com.ymo.data.local.prefs.AppPreferencesImpl
import com.ymo.data.local.prefs.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferencesHelper(pref: SharedPreferences): PreferencesHelper =
        AppPreferencesImpl(pref)

}
