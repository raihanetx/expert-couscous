package com.horizonbrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizonbrowser.domain.model.*
import com.horizonbrowser.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val tabRepository: TabRepository,
    private val historyRepository: HistoryRepository,
    private val favoritesRepository: FavoritesRepository,
    private val zoomRepository: ZoomRepository,
    private val cookieRepository: CookieRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BrowserState())
    val state: StateFlow<BrowserState> = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    sealed class NavigationEvent {
        data class LoadUrl(val tabId: String, val url: String) : NavigationEvent()
        data class SetZoom(val tabId: String, val zoomLevel: Int) : NavigationEvent()
        data class SetDesktopMode(val tabId: String, val enabled: Boolean) : NavigationEvent()
        data class Refresh(val tabId: String) : NavigationEvent()
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                tabRepository.getAllTabs(),
                historyRepository.getAllHistory(),
                favoritesRepository.getAllFavorites(),
                zoomRepository.getAllZoomPreferences(),
                cookieRepository.getBlockedDomains(),
                cookieRepository.getCookieMode(),
                preferencesRepository.getDefaultZoom(),
                preferencesRepository.getDesktopModeDefault()
            ) { tabs, history, favorites, zoomPrefs, blocked, cookieMode, defaultZoom, desktopDefault ->
                BrowserState(
                    tabs = tabs,
                    history = history,
                    favorites = favorites,
                    zoomPreferences = zoomPrefs.associate { it.domain to it.zoomLevel },
                    blockedDomains = blocked,
                    cookieMode = cookieMode,
                    defaultZoom = defaultZoom,
                    desktopModeDefault = desktopDefault,
                    currentTabId = _state.value.currentTabId ?: tabs.firstOrNull()?.tabId
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun onEvent(event: BrowserEvent) {
        when (event) {
            is BrowserEvent.LoadUrl -> loadUrl(event.url)
            is BrowserEvent.Search -> search(event.query)
            is BrowserEvent.GoBack -> goBack()
            is BrowserEvent.GoForward -> goForward()
            is BrowserEvent.Refresh -> refresh()
            is BrowserEvent.AddNewTab -> addNewTab()
            is BrowserEvent.SwitchTab -> switchTab(event.tabId)
            is BrowserEvent.CloseTab -> closeTab(event.tabId)
            is BrowserEvent.SetZoom -> setZoom(event.zoomLevel)
            is BrowserEvent.ToggleDesktopMode -> toggleDesktopMode(event.tabId)
            is BrowserEvent.SetCookieMode -> setCookieMode(event.mode)
            is BrowserEvent.BlockDomain -> blockDomain(event.domain)
            is BrowserEvent.UnblockDomain -> unblockDomain(event.domain)
            is BrowserEvent.ExportBlockedDomains -> exportBlockedDomains()
            is BrowserEvent.AddToFavorites -> addToFavorites(event.url, event.title)
            is BrowserEvent.RemoveFromFavorites -> removeFromFavorites(event.url)
            is BrowserEvent.UpdateTabUrl -> updateTabUrl(event.tabId, event.url, event.title)
            is BrowserEvent.ToggleSearchOverlay -> toggleSearchOverlay()
            is BrowserEvent.ToggleTabsOverlay -> toggleTabsOverlay()
            is BrowserEvent.ToggleMenuSheet -> toggleMenuSheet()
            is BrowserEvent.ToggleCookieManager -> toggleCookieManager()
            is BrowserEvent.ClearHistory -> clearHistory()
        }
    }

    private fun loadUrl(url: String) {
        val currentTabId = _state.value.currentTabId ?: return
        val formattedUrl = formatUrl(url)
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.LoadUrl(currentTabId, formattedUrl))
            historyRepository.addToHistory(
                HistoryItem(id = 0, url = formattedUrl, title = formattedUrl)
            )
        }
    }

    private fun search(query: String) {
        val searchUrl = if (query.startsWith("http")) {
            query
        } else {
            "https://www.google.com/search?q=${java.net.URLEncoder.encode(query, "UTF-8")}"
        }
        loadUrl(searchUrl)
        _state.update { it.copy(showSearchOverlay = false, searchQuery = "") }
    }

    private fun goBack() {
        // Handled by WebView
    }

    private fun goForward() {
        // Handled by WebView
    }

    private fun refresh() {
        val currentTabId = _state.value.currentTabId ?: return
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.Refresh(currentTabId))
        }
    }

    private fun addNewTab() {
        val newTabId = UUID.randomUUID().toString()
        viewModelScope.launch {
            tabRepository.addTab(
                Tab(
                    tabId = newTabId,
                    url = "https://www.google.com",
                    title = "New Tab",
                    position = _state.value.tabs.size,
                    isDesktopMode = _state.value.desktopModeDefault
                )
            )
            _state.update { it.copy(currentTabId = newTabId, showTabsOverlay = false) }
        }
    }

    private fun switchTab(tabId: String) {
        _state.update { it.copy(currentTabId = tabId, showTabsOverlay = false) }
    }

    private fun closeTab(tabId: String) {
        viewModelScope.launch {
            tabRepository.removeTab(tabId)
            val remainingTabs = _state.value.tabs.filter { it.tabId != tabId }
            _state.update {
                it.copy(
                    currentTabId = if (it.currentTabId == tabId) {
                        remainingTabs.firstOrNull()?.tabId
                    } else it.currentTabId
                )
            }
        }
    }

    private fun setZoom(zoomLevel: Int) {
        val currentTab = _state.value.currentTab ?: return
        val domain = BrowserState.extractDomain(currentTab.url)
        viewModelScope.launch {
            zoomRepository.setZoomForDomain(domain, zoomLevel)
            _navigationEvents.emit(NavigationEvent.SetZoom(currentTab.tabId, zoomLevel))
        }
    }

    private fun toggleDesktopMode(tabId: String) {
        val tab = _state.value.tabs.find { it.tabId == tabId } ?: return
        viewModelScope.launch {
            tabRepository.updateTab(tab.copy(isDesktopMode = !tab.isDesktopMode))
            _navigationEvents.emit(NavigationEvent.SetDesktopMode(tabId, !tab.isDesktopMode))
        }
    }

    private fun setCookieMode(mode: CookieMode) {
        viewModelScope.launch {
            cookieRepository.setCookieMode(mode)
            _state.update { it.copy(cookieMode = mode) }
        }
    }

    private fun blockDomain(domain: String) {
        viewModelScope.launch {
            cookieRepository.blockDomain(domain)
        }
    }

    private fun unblockDomain(domain: String) {
        viewModelScope.launch {
            cookieRepository.unblockDomain(domain)
        }
    }

    private fun exportBlockedDomains() {
        viewModelScope.launch {
            val json = cookieRepository.exportBlockedDomains()
            // Handle export - could save to file or share
        }
    }

    private fun addToFavorites(url: String, title: String) {
        viewModelScope.launch {
            favoritesRepository.addToFavorites(Favorite(url, title))
        }
    }

    private fun removeFromFavorites(url: String) {
        viewModelScope.launch {
            favoritesRepository.removeFromFavorites(url)
        }
    }

    private fun updateTabUrl(tabId: String, url: String, title: String) {
        viewModelScope.launch {
            val tab = tabRepository.getTabById(tabId) ?: return@launch
            tabRepository.updateTab(tab.copy(url = url, title = title))
        }
    }

    private fun toggleSearchOverlay() {
        _state.update { it.copy(showSearchOverlay = !it.showSearchOverlay) }
    }

    private fun toggleTabsOverlay() {
        _state.update { it.copy(showTabsOverlay = !it.showTabsOverlay) }
    }

    private fun toggleMenuSheet() {
        _state.update { it.copy(showMenuSheet = !it.showMenuSheet) }
    }

    private fun toggleCookieManager() {
        _state.update { it.copy(showCookieManager = !it.showCookieManager) }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }

    private fun formatUrl(url: String): String {
        return if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            "https://$url"
        }
    }
}
