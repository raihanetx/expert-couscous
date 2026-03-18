package com.horizonbrowser.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.horizonbrowser.domain.model.*
import com.horizonbrowser.domain.model.BrowserState
import com.horizonbrowser.presentation.viewmodel.BrowserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserApp(
    viewModel: BrowserViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        if (state.tabs.isEmpty()) {
            viewModel.onEvent(BrowserEvent.AddNewTab)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.currentTab?.title ?: "Horizon Browser",
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(BrowserEvent.Refresh) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { viewModel.onEvent(BrowserEvent.ToggleTabsOverlay) }) {
                        Icon(Icons.Default.Tab, contentDescription = "Tabs")
                    }
                    IconButton(onClick = { viewModel.onEvent(BrowserEvent.ToggleMenuSheet) }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                IconButton(
                    onClick = { viewModel.onEvent(BrowserEvent.GoBack) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                IconButton(
                    onClick = { viewModel.onEvent(BrowserEvent.GoForward) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
                }
                IconButton(
                    onClick = { viewModel.onEvent(BrowserEvent.ToggleSearchOverlay) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(
                    onClick = { viewModel.onEvent(BrowserEvent.AddNewTab) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Tab")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            state.currentTab?.let { tab ->
                WebViewContainer(
                    tab = tab,
                    zoomLevel = state.zoomPreferences[BrowserState.extractDomain(tab.url)] ?: state.defaultZoom,
                    cookieMode = state.cookieMode,
                    onUrlLoaded = { url, title ->
                        viewModel.onEvent(BrowserEvent.UpdateTabUrl(tab.tabId, url, title))
                    }
                )
            }

            if (state.showSearchOverlay) {
                SearchOverlay(
                    query = state.searchQuery,
                    onQueryChange = { },
                    onSearch = { viewModel.onEvent(BrowserEvent.Search(it)) },
                    onDismiss = { viewModel.onEvent(BrowserEvent.ToggleSearchOverlay) },
                    history = state.history
                )
            }

            if (state.showTabsOverlay) {
                TabPopupOverlay(
                    tabs = state.tabs,
                    currentTabId = state.currentTabId,
                    onTabSelected = { viewModel.onEvent(BrowserEvent.SwitchTab(it)) },
                    onTabClosed = { viewModel.onEvent(BrowserEvent.CloseTab(it)) },
                    onNewTab = { viewModel.onEvent(BrowserEvent.AddNewTab) },
                    onDismiss = { viewModel.onEvent(BrowserEvent.ToggleTabsOverlay) }
                )
            }

            if (state.showMenuSheet) {
                MenuBottomSheet(
                    currentZoom = state.currentZoom,
                    isDesktopMode = state.currentTab?.isDesktopMode ?: false,
                    cookieMode = state.cookieMode,
                    isFavorite = state.favorites.any { it.url == state.currentTab?.url },
                    onZoomChanged = { viewModel.onEvent(BrowserEvent.SetZoom(it)) },
                    onDesktopModeToggled = {
                        state.currentTab?.tabId?.let {
                            viewModel.onEvent(BrowserEvent.ToggleDesktopMode(it))
                        }
                    },
                    onCookieModeChanged = { viewModel.onEvent(BrowserEvent.SetCookieMode(it)) },
                    onToggleFavorite = {
                        state.currentTab?.let { tab ->
                            if (state.favorites.any { it.url == tab.url }) {
                                viewModel.onEvent(BrowserEvent.RemoveFromFavorites(tab.url))
                            } else {
                                viewModel.onEvent(BrowserEvent.AddToFavorites(tab.url, tab.title))
                            }
                        }
                    },
                    onCookieManagerOpened = { viewModel.onEvent(BrowserEvent.ToggleCookieManager) },
                    onClearHistory = { viewModel.onEvent(BrowserEvent.ClearHistory) },
                    onDismiss = { viewModel.onEvent(BrowserEvent.ToggleMenuSheet) }
                )
            }

            if (state.showCookieManager) {
                CookieManagerOverlay(
                    blockedDomains = state.blockedDomains,
                    cookieMode = state.cookieMode,
                    onCookieModeChanged = { viewModel.onEvent(BrowserEvent.SetCookieMode(it)) },
                    onBlockDomain = { viewModel.onEvent(BrowserEvent.BlockDomain(it)) },
                    onUnblockDomain = { viewModel.onEvent(BrowserEvent.UnblockDomain(it)) },
                    onExport = { viewModel.onEvent(BrowserEvent.ExportBlockedDomains) },
                    onDismiss = { viewModel.onEvent(BrowserEvent.ToggleCookieManager) }
                )
            }
        }
    }
}
