package com.evenly.faceup.data.model

data class User(
    val id: String,
    val phone: String,
    val name: String,
    val avatarUrl: String? = null,
    val status: String = "offline",
    val createdAt: String,
    val updatedAt: String
)

data class Connection(
    val id: String,
    val requesterId: String,
    val receiverId: String,
    val status: ConnectionStatus,
    val createdAt: String,
    val updatedAt: String
)

enum class ConnectionStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}

data class Call(
    val id: String,
    val callerId: String,
    val receiverId: String,
    val status: CallStatus,
    val startedAt: String? = null,
    val endedAt: String? = null,
    val createdAt: String
)

enum class CallStatus {
    RINGING,
    CONNECTED,
    ENDED,
    MISSED
}

data class AuthResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val user: AuthUser
)

data class AuthUser(
    val id: String,
    val phone: String,
    val email: String?,
    val created_at: String
)

data class FcmToken(
    val userId: String,
    val token: String,
    val updatedAt: String
)
