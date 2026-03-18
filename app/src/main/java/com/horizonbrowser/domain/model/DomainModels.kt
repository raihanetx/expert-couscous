package com.horizonbrowser.domain.model

data class Tab(
    val tabId: String,
    val url: String,
    val title: String,
    val position: Int,
    val isDesktopMode: Boolean = false
)

data class HistoryItem(
    val id: Long,
    val url: String,
    val title: String,
    val visitedAt: Long
)

data class Favorite(
    val url: String,
    val title: String,
    val position: Int = 0
)

data class ZoomSetting(
    val domain: String,
    val zoomLevel: Int
)

data class BlockedDomain(
    val domain: String
)

enum class CookieMode {
    ALLOW_ALL,
    BLOCK_THIRD_PARTY,
    BLOCK_ALL
}
