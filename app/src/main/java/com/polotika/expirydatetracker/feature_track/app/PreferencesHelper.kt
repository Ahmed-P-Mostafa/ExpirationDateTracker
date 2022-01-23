package com.polotika.expirydatetracker.feature_track.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.polotika.expirydatetracker.feature_track.domain.repository.PreferencesRepository
import com.polotika.expirydatetracker.utils.AppConstants
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(AppConstants.PREFERENCES_NAME)

class PreferencesHelper @Inject constructor(private val context: Context) :PreferencesRepository{

    private val isAppFirstTimeRun = booleanPreferencesKey(AppConstants.PREFERENCES_WORKER_KEY)

    override suspend fun setFirstTime(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[isAppFirstTimeRun] = value

        }
    }

    override fun getFirstTime() = context.dataStore.data.map { prefs ->
        prefs[isAppFirstTimeRun] ?: true
    }
}