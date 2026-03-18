package com.horizonbrowser.presentation.screens

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.horizonbrowser.domain.model.Tab
import com.horizonbrowser.domain.model.CookieMode
import com.horizonbrowser.webview.HorizonWebViewManager

@Composable
fun WebViewContainer(
    tab: Tab,
    zoomLevel: Int,
    cookieMode: CookieMode,
    onUrlLoaded: (String, String) -> Unit
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    
    val webViewManager = remember {
        HorizonWebViewManager(
            context = context,
            onPageLoaded = { url, title -> onUrlLoaded(url, title) },
            onZoomChanged = { }
        )
    }

    LaunchedEffect(zoomLevel) {
        webViewManager.setZoom(webView, zoomLevel)
    }

    LaunchedEffect(tab.isDesktopMode) {
        webViewManager.setDesktopMode(webView, tab.isDesktopMode)
    }

    LaunchedEffect(cookieMode) {
        webViewManager.setCookieMode(cookieMode)
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            webViewManager.createWebView().apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webView = this
                loadUrl(tab.url)
            }
        },
        update = { view ->
            if (view.url != tab.url) {
                view.loadUrl(tab.url)
            }
        },
        onRelease = { view ->
            webViewManager.cleanup(view)
        }
    )
}
