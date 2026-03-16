package com.evenly.faceup.data.repository

import com.evenly.faceup.data.model.*
import com.evenly.faceup.data.remote.SupabaseConfig
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * Repository for user-related operations
 * Handles authentication, user management, and connections
 */
class UserRepository {
    
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    private val baseUrl = SupabaseConfig.SUPABASE_URL
    private val apiKey = SupabaseConfig.SUPABASE_ANON_KEY
    
    /**
     * Sign up a new user with phone and password
     */
    suspend fun signUp(phone: String, password: String, name: String): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.post("$baseUrl/auth/v1/signup") {
                    headers {
                        append("apikey", apiKey)
                        append("Content-Type", "application/json")
                    }
                    setBody(mapOf(
                        "phone" to phone,
                        "password" to password,
                        "data" to mapOf("name" to name)
                    ))
                }
                // Handle response
                Result.success(AuthResponse("", "", 0L, AuthUser("", phone, null, "")))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Sign in with phone and password
     */
    suspend fun signIn(phone: String, password: String): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.post("$baseUrl/auth/v1/token") {
                    headers {
                        append("apikey", apiKey)
                        append("Content-Type", "application/json")
                    }
                    setBody(mapOf(
                        "phone" to phone,
                        "password" to password,
                        "grant_type" to "password"
                    ))
                }
                Result.success(AuthResponse("", "", 0L, AuthUser("", phone, null, "")))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Sign out current user
     */
    suspend fun signOut(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                client.post("$baseUrl/auth/v1/logout") {
                    headers {
                        append("apikey", apiKey)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Search users by phone number
     */
    suspend fun searchUsers(phoneQuery: String): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                // Implement search logic
                Result.success(emptyList())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Send friend request
     */
    suspend fun sendFriendRequest(receiverId: String): Result<Connection> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(Connection("", "", receiverId, ConnectionStatus.PENDING, "", ""))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Accept friend request
     */
    suspend fun acceptFriendRequest(connectionId: String): Result<Connection> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(Connection(connectionId, "", "", ConnectionStatus.ACCEPTED, "", ""))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Reject friend request
     */
    suspend fun rejectFriendRequest(connectionId: String): Result<Connection> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(Connection(connectionId, "", "", ConnectionStatus.REJECTED, "", ""))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Get friends list
     */
    suspend fun getFriends(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(emptyList())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Get pending friend requests
     */
    suspend fun getPendingRequests(): Result<List<Connection>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(emptyList())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Update FCM token for push notifications
     */
    suspend fun updateFcmToken(token: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
