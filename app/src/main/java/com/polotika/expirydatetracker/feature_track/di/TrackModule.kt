package com.polotika.expirydatetracker.feature_track.di

import android.content.Context
import com.polotika.expirydatetracker.feature_track.app.PreferencesHelper
import com.polotika.expirydatetracker.feature_track.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackModule {


    @Provides
    @Singleton
    fun providesPreferencesHelper(@ApplicationContext context: Context):PreferencesRepository{
        return PreferencesHelper(context = context)
    }
}