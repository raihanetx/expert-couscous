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
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    val isNameValid = name.length >= 2
    val isPhoneValid = phone.length >= 11
    val isPasswordValid = password.length >= 6
    val isConfirmValid = confirmPassword == password && confirmPassword.isNotEmpty()
    val isFormValid = isNameValid && isPhoneValid && isPasswordValid && isConfirmValid
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Primary, Secondary))),
            contentAlignment = Alignment.Center
        ) {
            Text("F", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("অ্যাকাউন্ট তৈরি করুন", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("FaceUp এ যুক্ত হন", fontSize = 14.sp, color = TextSecondary)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("আপনার নাম") },
            leadingIcon = { Icon(Icons.Default.Person, null, tint = TextSecondary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Phone
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
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("পাসওয়ার্ড") },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextSecondary) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        null, tint = TextSecondary
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(12.dp),
            isError = password.isNotEmpty() && !isPasswordValid
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("পাসওয়ার্ড নিশ্চিত করুন") },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextSecondary) },
            trailingIcon = {
                if (isConfirmValid) Icon(Icons.Default.CheckCircle, null, tint = Success)
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(12.dp),
            isError = confirmPassword.isNotEmpty() && !isConfirmValid
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { isLoading = true; onRegisterSuccess() },
            enabled = isFormValid && !isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
            else Text("রেজিস্টার করুন", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row {
            Text("ইতিমধ্যে অ্যাকাউন্ট আছে? ", color = TextSecondary, fontSize = 14.sp)
            TextButton(onClick = onNavigateToLogin) {
                Text("এখানে লগইন করুন", color = Primary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
