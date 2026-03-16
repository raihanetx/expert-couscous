package com.evenly.faceup.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.evenly.faceup.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) { popUpTo(0) } },
                onNavigateToDashboard = { navController.navigate(Screen.Dashboard.route) { popUpTo(0) } }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { navController.navigate(Screen.Dashboard.route) { popUpTo(0) } }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Screen.Dashboard.route) { popUpTo(0) } }
            )
        }
        
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToFriends = { navController.navigate(Screen.Friends.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToVideoCall = { userId -> navController.navigate(Screen.VideoCall.createRoute(userId)) }
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onUserClick = { userId -> navController.navigate(Screen.UserProfile.createRoute(userId)) }
            )
        }
        
        composable(Screen.Friends.route) {
            FriendsScreen(
                onBack = { navController.popBackStack() },
                onUserClick = { userId -> navController.navigate(Screen.UserProfile.createRoute(userId)) }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = { navController.navigate(Screen.Login.route) { popUpTo(0) } }
            )
        }
        
        composable(
            route = Screen.VideoCall.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            VideoCallScreen(
                onEndCall = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.UserProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            UserProfileScreen(
                onBack = { navController.popBackStack() },
                onStartCall = { userId -> navController.navigate(Screen.VideoCall.createRoute(userId)) }
            )
        }
    }
}
