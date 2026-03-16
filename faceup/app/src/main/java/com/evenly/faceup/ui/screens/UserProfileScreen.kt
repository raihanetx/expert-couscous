package com.evenly.faceup.ui.screens

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
fun UserProfileScreen(
    onBack: () -> Unit,
    onStartCall: (String) -> Unit
) {
    var connectionStatus by remember { mutableStateOf("none") } // none, pending, friends
    
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("প্রোফাইল", color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Primary, Secondary))),
                contentAlignment = Alignment.Center
            ) {
                Text("?", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("ইউজার", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("01XXXXXXXXX", fontSize = 16.sp, color = TextSecondary)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Online))
                Spacer(modifier = Modifier.width(6.dp))
                Text("অনলাইন", fontSize = 14.sp, color = Online)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            when (connectionStatus) {
                "friends" -> {
                    Button(
                        onClick = { onStartCall("user_1") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Success)
                    ) {
                        Icon(Icons.Default.Videocam, null)
                        Spacer(Modifier.width(8.dp))
                        Text("ভিডিও কল করুন", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                "pending" -> {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = false
                    ) {
                        Icon(Icons.Default.HourglassEmpty, null)
                        Spacer(Modifier.width(8.dp))
                        Text("রিকোয়েস্ট পেন্ডিং", fontSize = 16.sp)
                    }
                }
                else -> {
                    Button(
                        onClick = { connectionStatus = "pending" },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Default.PersonAdd, null)
                        Spacer(Modifier.width(8.dp))
                        Text("ফ্রেন্ড রিকোয়েস্ট পাঠান", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
