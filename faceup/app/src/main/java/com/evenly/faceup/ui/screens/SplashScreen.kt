package com.evenly.faceup.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evenly.faceup.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    // Animation states
    var showSkeleton by remember { mutableStateOf(true) }
    var showHeader by remember { mutableStateOf(false) }
    var skeletonAlpha by remember { mutableFloatStateOf(1f) }
    
    // Shimmer animation
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
    
    // Fade animations
    val skeletonFade = remember { Animatable(1f) }
    val headerFade = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        delay(1500) // Show skeleton for 1.5 seconds
        
        // Fade out skeleton
        skeletonFade.animateTo(0f, tween(500, easing = EaseOutCubic))
        showSkeleton = false
        
        // Fade in header
        headerFade.animateTo(1f, tween(500, easing = EaseInCubic))
        showHeader = true
        
        delay(800) // Wait for animation to complete
        
        // Navigate (always go to login for demo)
        onNavigateToLogin()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with fade-in animation
            AnimatedVisibility(
                visible = showHeader,
                enter = fadeIn(tween(500)),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Primary, Secondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "F",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "FaceUp",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "ভিডিও কল দিয়ে সংযুক্ত থাকুন",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Skeleton Loading with fade-out animation
            AnimatedVisibility(
                visible = showSkeleton,
                enter = fadeIn(),
                exit = fadeOut(tween(500))
            ) {
                Column {
                    repeat(3) {
                        SkeletonCard(shimmerOffset)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        
        // Version text at bottom
        AnimatedVisibility(
            visible = showHeader,
            enter = fadeIn(tween(500)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = "v1.0.0",
                modifier = Modifier.padding(bottom = 32.dp),
                fontSize = 12.sp,
                color = TextHint
            )
        }
    }
}

@Composable
fun SkeletonCard(shimmerOffset: Float) {
    val shimmerColors = listOf(SkeletonBase, SkeletonHighlight, SkeletonBase)
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(shimmerOffset - 200, 0f),
        end = Offset(shimmerOffset, 0f)
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SkeletonBase)
    ) {
        // Avatar skeleton
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(brush)
        )
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Name skeleton
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status skeleton
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
