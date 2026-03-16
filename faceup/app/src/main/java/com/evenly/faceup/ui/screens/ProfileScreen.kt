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
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("লগআউট করুন") },
            text = { Text("আপনি কি নিশ্চিত যে লগআউট করতে চান?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }, colors = ButtonDefaults.textButtonColors(contentColor = Error)) {
                    Text("লগআউট")
                }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("বাতিল") } }
        )
    }
    
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
                Text("U", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(onClick = {}) {
                Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("ছবি পরিবর্তন", fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("ব্যবহারকারী", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("01XXXXXXXXX", fontSize = 16.sp, color = TextSecondary)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ProfileItem(Icons.Default.Edit, "প্রোফাইল এডিট করুন") {}
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = TextHint.copy(alpha = 0.2f))
                    ProfileItem(Icons.Default.Notifications, "নোটিফিকেশন সেটিংস") {}
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = TextHint.copy(alpha = 0.2f))
                    ProfileItem(Icons.Default.PrivacyTip, "গোপনীয়তা") {}
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = TextHint.copy(alpha = 0.2f))
                    ProfileItem(Icons.Default.Help, "সাহায্য") {}
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Error)
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("লগআউট", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("FaceUp v1.0.0", fontSize = 12.sp, color = TextHint)
        }
    }
}

@Composable
fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Primary, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, color = TextPrimary, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, tint = TextHint)
    }
}
