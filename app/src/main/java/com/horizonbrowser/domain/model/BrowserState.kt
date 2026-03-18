package com.horizonbrowser.domain.model

data class BrowserState(
    val tabs: List<Tab> = emptyList(),
    val currentTabId: String? = null,
    val history: List<HistoryItem> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
    val zoomPreferences: Map<String, Int> = emptyMap(),
    val defaultZoom: Int = 100,
    val cookieMode: CookieMode = CookieMode.ALLOW_ALL,
    val blockedDomains: List<BlockedDomain> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSearchOverlay: Boolean = false,
    val showTabsOverlay: Boolean = false,
    val showMenuSheet: Boolean = false,
    val showCookieManager: Boolean = false,
    val searchQuery: String = "",
    val desktopModeDefault: Boolean = false
) {
    val currentTab: Tab?
        get() = tabs.find { it.tabId == currentTabId }
    
    val currentZoom: Int
        get() = currentTab?.let { tab ->
            val domain = extractDomain(tab.url)
            zoomPreferences[domain] ?: defaultZoom
        } ?: defaultZoom
    
    companion object {
        fun extractDomain(url: String): String {
            return try {
                val uri = java.net.URI(url)
                uri.host ?: url
            } catch (e: Exception) {
                url
            }
        }
    }
}
