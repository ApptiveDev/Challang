package com.stellan.challang.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stellan.challang.ui.screen.home.MainScreen
import com.stellan.challang.util.TokenManager
import com.stellan.challang.ui.viewmodel.AuthViewModel
import com.stellan.challang.ui.viewmodel.AuthViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx. compose. runtime. collectAsState
import androidx. compose. runtime.LaunchedEffect
import com. stellan. challang. data. api. ApiClient
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(ApiClient.api))
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        val currentDestination = navController.currentBackStackEntry?.destination?.route

        if (!isLoggedIn && currentDestination != "auth") {
            navController.navigate("auth") {
                popUpTo("main") { inclusive = true }
                launchSingleTop = true
            }
        } else if (isLoggedIn && currentDestination != "main") {
            navController.navigate("main") {
                popUpTo("auth") { inclusive = true }
                launchSingleTop = true
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "main" else "auth"
    ) {
        authNavGraph(navController)
        composable("main") { MainScreen(navController) }
    }
}


