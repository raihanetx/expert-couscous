package com.horizonbrowser.webview

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.*
import com.horizonbrowser.domain.model.CookieMode

@SuppressLint("SetJavaScriptEnabled")
class HorizonWebViewManager(
    private val context: Context,
    private val onPageLoaded: (String, String) -> Unit,
    private val onZoomChanged: (Int) -> Unit
) {
    private var currentZoom: Int = 100
    private var isDesktopMode: Boolean = false
    private var cookieMode: CookieMode = CookieMode.ALLOW_ALL

    fun createWebView(): WebView {
        return WebView(context).apply {
            setupWebViewDefaults()
            webViewClient = createWebViewClient()
            webChromeClient = createWebChromeClient()
        }
    }

    private fun WebView.setupWebViewDefaults() {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            cacheMode = WebSettings.LOAD_DEFAULT
            setSupportZoom(true)
            allowFileAccess = true
            allowContentAccess = true
        }
    }

    private fun createWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                url?.let {
                    val title = view?.title ?: url
                    onPageLoaded(it, title)
                }
                applyZoomScript(view)
            }
        }
    }

    private fun createWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }
        }
    }

    fun setZoom(webView: WebView?, zoomLevel: Int) {
        currentZoom = zoomLevel.coerceIn(50, 150)
        webView?.let { applyZoomScript(it) }
        onZoomChanged(currentZoom)
    }

    private fun applyZoomScript(webView: WebView?) {
        val scale = currentZoom / 100f
        val script = """
            (function() {
                document.documentElement.style.transform = 'scale($scale)';
                document.documentElement.style.transformOrigin = 'top left';
                document.documentElement.style.width = '${100f / scale}%';
            })();
        """.trimIndent()
        webView?.evaluateJavascript(script, null)
    }

    fun setDesktopMode(webView: WebView?, enabled: Boolean) {
        isDesktopMode = enabled
        webView?.settings?.apply {
            userAgentString = if (enabled) {
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"
            } else {
                null
            }
        }
        webView?.reload()
    }

    fun setCookieMode(mode: CookieMode) {
        cookieMode = mode
        val cookieManager = CookieManager.getInstance()
        when (mode) {
            CookieMode.ALLOW_ALL -> {
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(null, true)
            }
            CookieMode.BLOCK_THIRD_PARTY -> {
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(null, false)
            }
            CookieMode.BLOCK_ALL -> {
                cookieManager.setAcceptCookie(false)
                cookieManager.setAcceptThirdPartyCookies(null, false)
            }
        }
    }

    fun loadUrl(webView: WebView?, url: String) {
        webView?.loadUrl(url)
    }

    fun goBack(webView: WebView?): Boolean {
        return if (webView?.canGoBack() == true) {
            webView.goBack()
            true
        } else {
            false
        }
    }

    fun goForward(webView: WebView?) {
        if (webView?.canGoForward() == true) {
            webView.goForward()
        }
    }

    fun refresh(webView: WebView?) {
        webView?.reload()
    }

    fun clearCookies() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
    }

    fun cleanup(webView: WebView?) {
        webView?.apply {
            stopLoading()
            settings.javaScriptEnabled = false
            clearHistory()
            removeAllViews()
            destroy()
        }
    }
}
