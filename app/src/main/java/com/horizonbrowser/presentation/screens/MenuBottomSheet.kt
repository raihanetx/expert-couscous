package com.horizonbrowser.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.horizonbrowser.domain.model.CookieMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    currentZoom: Int,
    isDesktopMode: Boolean,
    cookieMode: CookieMode,
    isFavorite: Boolean,
    onZoomChanged: (Int) -> Unit,
    onDesktopModeToggled: () -> Unit,
    onCookieModeChanged: (CookieMode) -> Unit,
    onToggleFavorite: () -> Unit,
    onCookieManagerOpened: () -> Unit,
    onClearHistory: () -> Unit,
    onDismiss: () -> Unit
) {
    var zoomSliderValue by remember { mutableFloatStateOf(currentZoom.toFloat()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Page Zoom: ${zoomSliderValue.toInt()}%",
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = zoomSliderValue,
                onValueChange = { zoomSliderValue = it },
                valueRange = 50f..150f,
                onValueChangeFinished = { onZoomChanged(zoomSliderValue.toInt()) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Computer, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Desktop Mode")
                }
                Switch(
                    checked = isDesktopMode,
                    onCheckedChange = { onDesktopModeToggled() }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Cookie Settings",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            CookieModeSelector(
                currentMode = cookieMode,
                onModeSelected = onCookieModeChanged
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isFavorite) "Remove from Favorites" else "Add to Favorites")
                }
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            TextButton(onClick = onCookieManagerOpened) {
                Icon(Icons.Default.Cookie, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cookie Manager")
            }

            TextButton(onClick = onClearHistory) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clear History")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CookieModeSelector(
    currentMode: CookieMode,
    onModeSelected: (CookieMode) -> Unit
) {
    Column {
        CookieModeOption(
            text = "Allow All Cookies",
            selected = currentMode == CookieMode.ALLOW_ALL,
            onClick = { onModeSelected(CookieMode.ALLOW_ALL) }
        )
        CookieModeOption(
            text = "Block Third-Party Cookies",
            selected = currentMode == CookieMode.BLOCK_THIRD_PARTY,
            onClick = { onModeSelected(CookieMode.BLOCK_THIRD_PARTY) }
        )
        CookieModeOption(
            text = "Block All Cookies",
            selected = currentMode == CookieMode.BLOCK_ALL,
            onClick = { onModeSelected(CookieMode.BLOCK_ALL) }
        )
    }
}

@Composable
private fun CookieModeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
