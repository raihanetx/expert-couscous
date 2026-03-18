package com.horizonbrowser.domain.repository

import com.horizonbrowser.domain.model.*
import kotlinx.coroutines.flow.Flow

interface TabRepository {
    fun getAllTabs(): Flow<List<Tab>>
    suspend fun getTabById(tabId: String): Tab?
    suspend fun addTab(tab: Tab)
    suspend fun updateTab(tab: Tab)
    suspend fun removeTab(tabId: String)
    suspend fun clearAllTabs()
    fun getTabCount(): Flow<Int>
}

interface HistoryRepository {
    fun getAllHistory(): Flow<List<HistoryItem>>
    fun searchHistory(query: String): Flow<List<HistoryItem>>
    suspend fun addToHistory(item: HistoryItem)
    suspend fun removeFromHistory(id: Long)
    suspend fun clearHistory()
}

interface FavoritesRepository {
    fun getAllFavorites(): Flow<List<Favorite>>
    suspend fun addToFavorites(favorite: Favorite)
    suspend fun removeFromFavorites(url: String)
    suspend fun isFavorite(url: String): Boolean
}

interface ZoomRepository {
    suspend fun getZoomForDomain(domain: String): Int
    suspend fun setZoomForDomain(domain: String, zoomLevel: Int)
    fun getAllZoomPreferences(): Flow<List<ZoomSetting>>
}

interface CookieRepository {
    fun getCookieMode(): Flow<CookieMode>
    suspend fun setCookieMode(mode: CookieMode)
    fun getBlockedDomains(): Flow<List<BlockedDomain>>
    suspend fun blockDomain(domain: String)
    suspend fun unblockDomain(domain: String)
    suspend fun isDomainBlocked(domain: String): Boolean
    suspend fun exportBlockedDomains(): String
}

interface PreferencesRepository {
    fun getCookieModeFlow(): Flow<CookieMode>
    fun getDefaultZoom(): Flow<Int>
    fun getDesktopModeDefault(): Flow<Boolean>
    suspend fun setCookieMode(mode: CookieMode)
    suspend fun setDefaultZoom(zoom: Int)
    suspend fun setDesktopModeDefault(enabled: Boolean)
}
