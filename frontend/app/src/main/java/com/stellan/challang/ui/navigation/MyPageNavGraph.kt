package com.stellan.challang.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.stellan.challang.ui.screen.mypage.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com. stellan. challang. data. api. ApiClient

import com.stellan.challang.data.repository.AuthRepository
import androidx.compose.runtime.*


fun NavGraphBuilder.myPageNavGraph(
    rootNavController: NavHostController,
    bottomNavController: NavHostController
) {
    navigation(startDestination = "mypageMain", route = "mypage") {
        composable("mypageMain") {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            var showLoggedOutDialog by remember { mutableStateOf(false) }

            MyPageScreen(
                rootNavController = rootNavController,
                onHelpClick = { bottomNavController.navigate("help") },
                onLogoutClick = {
                },
                onWithdrawClick = { bottomNavController.navigate("withdraw") }
            )
        }
        composable("help") {
            HelpScreen(onPrivacy = { bottomNavController.navigate("privacy") },
                onTerms   = { bottomNavController.navigate("terms") })
        }
        composable("privacy") {
            PrivacyPolicyScreen(onBack = { bottomNavController.popBackStack() })
        }
        composable("terms") {
            TermsOfServiceScreen(onBack = { bottomNavController.popBackStack() })
        }
        composable("withdraw") {
            WithdrawScreen(onDone = {
                rootNavController.navigate("auth") {
                    popUpTo("main") { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
    }
}