package com.horizonbrowser.data.repository

import com.horizonbrowser.data.local.preferences.UserPreferencesManager
import com.horizonbrowser.domain.model.CookieMode
import com.horizonbrowser.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesManager: UserPreferencesManager
) : PreferencesRepository {
    
    override fun getCookieModeFlow(): Flow<CookieMode> {
        return preferencesManager.cookieMode.map { mode ->
            when (mode) {
                0 -> CookieMode.ALLOW_ALL
                1 -> CookieMode.BLOCK_THIRD_PARTY
                2 -> CookieMode.BLOCK_ALL
                else -> CookieMode.ALLOW_ALL
            }
        }
    }

    override fun getDefaultZoom(): Flow<Int> {
        return preferencesManager.defaultZoom
    }

    override fun getDesktopModeDefault(): Flow<Boolean> {
        return preferencesManager.desktopModeDefault
    }

    override suspend fun setCookieMode(mode: CookieMode) {
        val modeInt = when (mode) {
            CookieMode.ALLOW_ALL -> 0
            CookieMode.BLOCK_THIRD_PARTY -> 1
            CookieMode.BLOCK_ALL -> 2
        }
        preferencesManager.setCookieMode(modeInt)
    }

    override suspend fun setDefaultZoom(zoom: Int) {
        preferencesManager.setDefaultZoom(zoom)
    }

    override suspend fun setDesktopModeDefault(enabled: Boolean) {
        preferencesManager.setDesktopModeDefault(enabled)
    }
}
