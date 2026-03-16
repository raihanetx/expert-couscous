package com.evenly.faceup.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Search : Screen("search")
    object Friends : Screen("friends")
    object Profile : Screen("profile")
    object VideoCall : Screen("video_call/{userId}") {
        fun createRoute(userId: String) = "video_call/$userId"
    }
    object UserProfile : Screen("user_profile/{userId}") {
        fun createRoute(userId: String) = "user_profile/$userId"
    }
}
