package com.horizonbrowser.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "browser_tabs")
data class BrowserTab(
    @PrimaryKey
    val tabId: String,
    val url: String,
    val title: String,
    val position: Int,
    val isDesktopMode: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "browsing_history")
data class BrowsingHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String,
    val title: String,
    val visitedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_sites")
data class FavoriteSite(
    @PrimaryKey
    val url: String,
    val title: String,
    val position: Int = 0,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "zoom_preferences")
data class ZoomPreference(
    @PrimaryKey
    val domain: String,
    val zoomLevel: Int = 100
)

@Entity(tableName = "cookie_blocked_domains")
data class CookieBlockedDomain(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val domain: String,
    val blockedAt: Long = System.currentTimeMillis()
)
