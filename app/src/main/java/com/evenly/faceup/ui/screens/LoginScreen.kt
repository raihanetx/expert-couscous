package com.evenly.faceup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evenly.faceup.ui.theme.*

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    val isPhoneValid = phone.length >= 11
    val isPasswordValid = password.length >= 6
    val isFormValid = isPhoneValid && isPasswordValid
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Logo
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Primary, Secondary))),
            contentAlignment = Alignment.Center
        ) {
            Text("F", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "FaceUp এ স্বাগতম",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Text(
            text = "ভিডিও কল দিয়ে সংযুক্ত থাকুন",
            fontSize = 14.sp,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Phone Input
        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 11 && it.all { c -> c.isDigit() }) phone = it },
            label = { Text("ফোন নম্বর") },
            placeholder = { Text("01XXXXXXXXX") },
            leadingIcon = { Icon(Icons.Default.Phone, null, tint = TextSecondary) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(12.dp),
            isError = phone.isNotEmpty() && !isPhoneValid
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("পাসওয়ার্ড") },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextSecondary) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        null,
                        tint = TextSecondary
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Login Button
        Button(
            onClick = { isLoading = true; onLoginSuccess() },
            enabled = isFormValid && !isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("লগইন করুন", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row {
            Text("অ্যাকাউন্ট নেই? ", color = TextSecondary, fontSize = 14.sp)
            TextButton(onClick = onNavigateToRegister) {
                Text("এখনই রেজিস্টার করুন", color = Primary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = TextHint,
    focusedLabelColor = Primary,
    unfocusedLabelColor = TextSecondary,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = Primary,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)
