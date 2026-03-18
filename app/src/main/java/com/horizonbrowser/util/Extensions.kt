package com.horizonbrowser.util

import java.net.URI

fun String.extractDomain(): String {
    return try {
        val uri = URI(this)
        uri.host ?: this
    } catch (e: Exception) {
        this
    }
}

fun String.formatUrl(): String {
    return if (this.startsWith("http://") || this.startsWith("https://")) {
        this
    } else {
        "https://$this"
    }
}
