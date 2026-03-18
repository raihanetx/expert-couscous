package com.horizonbrowser.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "horizon_preferences")

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val COOKIE_MODE = intPreferencesKey("cookie_mode")
        private val DEFAULT_ZOOM = intPreferencesKey("default_zoom")
        private val DESKTOP_MODE_DEFAULT = booleanPreferencesKey("desktop_mode_default")
    }

    val cookieMode: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[COOKIE_MODE] ?: 0 }

    val defaultZoom: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[DEFAULT_ZOOM] ?: 100 }

    val desktopModeDefault: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[DESKTOP_MODE_DEFAULT] ?: false }

    suspend fun setCookieMode(mode: Int) {
        context.dataStore.edit { preferences ->
            preferences[COOKIE_MODE] = mode
        }
    }

    suspend fun setDefaultZoom(zoom: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_ZOOM] = zoom.coerceIn(50, 150)
        }
    }

    suspend fun setDesktopModeDefault(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DESKTOP_MODE_DEFAULT] = enabled
        }
    }
}
