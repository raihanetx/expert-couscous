package com.evenly.faceup.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evenly.faceup.data.model.Connection
import com.evenly.faceup.data.model.User
import com.evenly.faceup.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication screens
 */
class AuthViewModel : ViewModel() {
    
    private val repository = UserRepository()
    
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    fun signUp(phone: String, password: String, name: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.signUp(phone, password, name)
            _uiState.value = if (result.isSuccess) {
                AuthUiState.Success
            } else {
                AuthUiState.Error(result.exceptionOrNull()?.message ?: "Sign up failed")
            }
        }
    }
    
    fun signIn(phone: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.signIn(phone, password)
            _uiState.value = if (result.isSuccess) {
                _isAuthenticated.value = true
                AuthUiState.Success
            } else {
                AuthUiState.Error(result.exceptionOrNull()?.message ?: "Sign in failed")
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _isAuthenticated.value = false
            _uiState.value = AuthUiState.Idle
        }
    }
    
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

/**
 * ViewModel for friends and connections
 */
class FriendsViewModel : ViewModel() {
    
    private val repository = UserRepository()
    
    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends.asStateFlow()
    
    private val _pendingRequests = MutableStateFlow<List<Connection>>(emptyList())
    val pendingRequests: StateFlow<List<Connection>> = _pendingRequests.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadFriends() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getFriends()
            if (result.isSuccess) {
                _friends.value = result.getOrNull() ?: emptyList()
            }
            _isLoading.value = false
        }
    }
    
    fun loadPendingRequests() {
        viewModelScope.launch {
            val result = repository.getPendingRequests()
            if (result.isSuccess) {
                _pendingRequests.value = result.getOrNull() ?: emptyList()
            }
        }
    }
    
    fun acceptRequest(connectionId: String) {
        viewModelScope.launch {
            repository.acceptFriendRequest(connectionId)
            loadFriends()
            loadPendingRequests()
        }
    }
    
    fun rejectRequest(connectionId: String) {
        viewModelScope.launch {
            repository.rejectFriendRequest(connectionId)
            loadPendingRequests()
        }
    }
}

/**
 * ViewModel for user search
 */
class SearchViewModel : ViewModel() {
    
    private val repository = UserRepository()
    
    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    fun updateQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun searchUsers(phoneQuery: String) {
        viewModelScope.launch {
            _isSearching.value = true
            val result = repository.searchUsers(phoneQuery)
            if (result.isSuccess) {
                _searchResults.value = result.getOrNull() ?: emptyList()
            }
            _isSearching.value = false
        }
    }
    
    fun sendFriendRequest(receiverId: String) {
        viewModelScope.launch {
            repository.sendFriendRequest(receiverId)
        }
    }
    
    fun clearResults() {
        _searchResults.value = emptyList()
        _searchQuery.value = ""
    }
}
