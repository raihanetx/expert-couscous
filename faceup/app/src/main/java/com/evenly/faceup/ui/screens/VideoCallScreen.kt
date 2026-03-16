package com.evenly.faceup.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evenly.faceup.ui.theme.*

@Composable
fun VideoCallScreen(
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var callDuration by remember { mutableLongStateOf(0L) }
    var isConnecting by remember { mutableStateOf(true) }
    
    val minutes = (callDuration / 60).toInt()
    val seconds = (callDuration % 60).toInt()
    val durationText = String.format("%02d:%02d", minutes, seconds)
    
    LaunchedEffect(isConnecting) {
        if (!isConnecting) {
            while (true) {
                kotlinx.coroutines.delay(1000)
                callDuration++
            }
        }
    }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        isConnecting = false
    }
    
    Box(modifier = Modifier.fillMaxSize().background(Black)) {
        // Remote Video Area
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isConnecting) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Primary, strokeWidth = 3.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("কানেক্টিং...", color = TextSecondary, fontSize = 16.sp)
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(Surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Remote Video", color = TextHint)
                }
            }
        }
        
        // Local Video (Small)
        Box(
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(120.dp)
                .clip(RoundedCornerShape(16.dp)).background(Surface),
            contentAlignment = Alignment.Center
        ) {
            if (!isCameraOff) Text("Local", color = TextHint, fontSize = 12.sp)
            else Icon(Icons.Default.VideocamOff, null, tint = TextSecondary, modifier = Modifier.size(32.dp))
        }
        
        // Caller Info
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(80.dp).clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Primary, Secondary))),
                contentAlignment = Alignment.Center
            ) {
                Text("?", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("বন্ধু", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(if (isConnecting) "কানেক্টিং..." else durationText, fontSize = 16.sp, color = if (isConnecting) TextSecondary else Success)
        }
        
        // Controls
        Card(
            modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp).fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallControlButton(
                    icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    backgroundColor = if (isMuted) Error else Color.White.copy(alpha = 0.2f),
                    onClick = { isMuted = !isMuted }
                )
                CallControlButton(
                    icon = if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                    backgroundColor = if (isCameraOff) Error else Color.White.copy(alpha = 0.2f),
                    onClick = { isCameraOff = !isCameraOff }
                )
                CallControlButton(
                    icon = Icons.Default.CallEnd,
                    backgroundColor = Error,
                    size = 64.dp,
                    onClick = onEndCall
                )
                CallControlButton(
                    icon = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                    backgroundColor = if (isSpeakerOn) Primary else Color.White.copy(alpha = 0.2f),
                    onClick = { isSpeakerOn = !isSpeakerOn }
                )
                CallControlButton(
                    icon = Icons.Default.Cameraswitch,
                    backgroundColor = Color.White.copy(alpha = 0.2f),
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun CallControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    size: androidx.compose.ui.unit.Dp = 52.dp,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(size).clip(CircleShape).background(backgroundColor)
    ) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
    }
}

@Composable
fun SkeletonCard(alpha: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    
    val shimmerColors = listOf(SkeletonBase, SkeletonHighlight, SkeletonBase)
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset(shimmerOffset - 200, 0f),
        end = androidx.compose.ui.geometry.Offset(shimmerOffset, 0f)
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SkeletonBase)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(brush)
        )
        Column(
            modifier = Modifier.weight(1f).padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(brush)
            )
        }
    }
}
