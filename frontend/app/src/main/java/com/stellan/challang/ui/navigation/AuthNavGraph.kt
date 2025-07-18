package com.stellan.challang.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.stellan.challang.ui.screen.auth.LoginScreen
import com.stellan.challang.ui.screen.auth.ProfileSettingScreen
import com.stellan.challang.ui.screen.auth.SignupScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.stellan.challang.data.repository.AuthRepository


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "login",
        route = "auth"
    ) {
        composable("login") {

            val context = LocalContext.current

            LaunchedEffect(Unit) {
                val authRepo = AuthRepository(context)
                authRepo.clearTokens()
            }
            LoginScreen(
                navController = navController,
                onLoginSuccess = { isNewUser, kakaoAccessToken, isPreferenceSet ->
                    if (isNewUser) {
                        navController.navigate("signup?token=$kakaoAccessToken") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        if (isPreferenceSet) {
                            navController.navigate("main") {
                                popUpTo("auth") { inclusive = true }
                            }
                        } else {
                            navController.navigate("profilesetting") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    }
                },
                onNeedSignup = { kakaoAccessToken ->
                    navController.navigate("signup?token=$kakaoAccessToken") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )


        }
        composable("signup?token={token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""

            SignupScreen(
                kakaoAccessToken = token,
                onSignupComplete = {
                    navController.navigate("profilesetting") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        composable("profilesetting") {
            ProfileSettingScreen(
                onProfileComplete = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
    }
}