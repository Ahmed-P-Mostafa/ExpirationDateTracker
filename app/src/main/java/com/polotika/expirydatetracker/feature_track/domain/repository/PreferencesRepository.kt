package com.polotika.expirydatetracker.feature_track.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getFirstTime(): Flow<Boolean>
    suspend fun setFirstTime(value :Boolean)
}