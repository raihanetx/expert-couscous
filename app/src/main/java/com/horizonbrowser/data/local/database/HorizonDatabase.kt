package com.horizonbrowser.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BrowserTab::class,
        BrowsingHistory::class,
        FavoriteSite::class,
        ZoomPreference::class,
        CookieBlockedDomain::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HorizonDatabase : RoomDatabase() {
    abstract fun browserTabDao(): BrowserTabDao
    abstract fun browsingHistoryDao(): BrowsingHistoryDao
    abstract fun favoriteSiteDao(): FavoriteSiteDao
    abstract fun zoomPreferenceDao(): ZoomPreferenceDao
    abstract fun cookieBlockedDomainDao(): CookieBlockedDomainDao
}
