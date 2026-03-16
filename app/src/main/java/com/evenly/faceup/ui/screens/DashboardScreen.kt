package com.evenly.faceup.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evenly.faceup.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToFriends: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToVideoCall: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    val friends = listOf("রহিম", "করিম", "সালাম")
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1500)
        isLoading = false
    }
    
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text("FaceUp", fontWeight = FontWeight.Bold, color = TextPrimary)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, "Profile", tint = TextPrimary)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // User Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(56.dp).clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Primary, Secondary))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("U", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("ব্যবহারকারী", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Online))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("অনলাইন", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Quick Actions
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(Icons.Outlined.Search, "ইউজার খুঁজুন", Modifier.weight(1f), onNavigateToSearch)
                QuickActionCard(Icons.Outlined.People, "বন্ধুরা", Modifier.weight(1f), onNavigateToFriends)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("আপনার বন্ধুরা", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            
            if (isLoading) {
                repeat(3) {
                    SkeletonCard(remember { mutableStateOf(0f) }.value)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else if (friends.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.PeopleOutline, null, tint = TextHint, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("কোন বন্ধু নেই", fontSize = 16.sp, color = TextSecondary)
                        Text("ফোন নম্বর দিয়ে বন্ধু খুঁজুন", fontSize = 14.sp, color = TextHint)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(friends) { friend ->
                        FriendItem(name = friend, onClick = { onNavigateToVideoCall("user_1") })
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(icon: ImageVector, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(100.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Primary, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun FriendItem(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Primary, Secondary))),
                contentAlignment = Alignment.Center
            ) {
                Text(name.firstOrNull()?.uppercase() ?: "?", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Online))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("অনলাইন", fontSize = 12.sp, color = TextSecondary)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Primary)
            ) {
                Icon(Icons.Default.Videocam, "Video Call", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}
