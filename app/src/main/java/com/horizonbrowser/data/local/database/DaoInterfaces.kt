package com.horizonbrowser.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BrowserTabDao {
    @Query("SELECT * FROM browser_tabs ORDER BY position")
    fun getAllTabs(): Flow<List<BrowserTab>>

    @Query("SELECT * FROM browser_tabs WHERE tabId = :tabId")
    suspend fun getTabById(tabId: String): BrowserTab?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTab(tab: BrowserTab)

    @Update
    suspend fun updateTab(tab: BrowserTab)

    @Delete
    suspend fun deleteTab(tab: BrowserTab)

    @Query("DELETE FROM browser_tabs WHERE tabId = :tabId")
    suspend fun deleteTabById(tabId: String)

    @Query("DELETE FROM browser_tabs")
    suspend fun deleteAllTabs()

    @Query("SELECT COUNT(*) FROM browser_tabs")
    fun getTabCount(): Flow<Int>
}

@Dao
interface BrowsingHistoryDao {
    @Query("SELECT * FROM browsing_history ORDER BY visitedAt DESC")
    fun getAllHistory(): Flow<List<BrowsingHistory>>

    @Query("SELECT * FROM browsing_history WHERE url LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' ORDER BY visitedAt DESC LIMIT 10")
    fun searchHistory(query: String): Flow<List<BrowsingHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: BrowsingHistory)

    @Query("DELETE FROM browsing_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Long)

    @Query("DELETE FROM browsing_history")
    suspend fun deleteAllHistory()
}

@Dao
interface FavoriteSiteDao {
    @Query("SELECT * FROM favorite_sites ORDER BY position")
    fun getAllFavorites(): Flow<List<FavoriteSite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(site: FavoriteSite)

    @Query("DELETE FROM favorite_sites WHERE url = :url")
    suspend fun deleteFavoriteByUrl(url: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_sites WHERE url = :url)")
    suspend fun isFavorite(url: String): Boolean
}

@Dao
interface ZoomPreferenceDao {
    @Query("SELECT * FROM zoom_preferences WHERE domain = :domain")
    suspend fun getZoomForDomain(domain: String): ZoomPreference?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setZoomForDomain(zoomPreference: ZoomPreference)

    @Query("SELECT * FROM zoom_preferences")
    fun getAllZoomPreferences(): Flow<List<ZoomPreference>>

    @Query("DELETE FROM zoom_preferences WHERE domain = :domain")
    suspend fun deleteZoomPreference(domain: String)
}

@Dao
interface CookieBlockedDomainDao {
    @Query("SELECT * FROM cookie_blocked_domains ORDER BY blockedAt DESC")
    fun getAllBlockedDomains(): Flow<List<CookieBlockedDomain>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun blockDomain(domain: CookieBlockedDomain)

    @Query("DELETE FROM cookie_blocked_domains WHERE domain = :domain")
    suspend fun unblockDomain(domain: String)

    @Query("SELECT EXISTS(SELECT 1 FROM cookie_blocked_domains WHERE domain = :domain)")
    suspend fun isDomainBlocked(domain: String): Boolean
}
