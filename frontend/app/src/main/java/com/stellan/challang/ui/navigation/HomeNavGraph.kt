package com.stellan.challang.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.stellan.challang.ui.screen.home.*

fun NavGraphBuilder.homeNavGraph(
    rootNavController: NavHostController,
    bottomNavController: NavHostController
) {
    navigation(startDestination = "curatingGuide", route = "home") {
        composable("curatingGuide") {
            CuratingGuideScreen(
                onComplete = { bottomNavController.navigate("curating") }
            )
        }
        composable("curating") {
            CuratingScreen(
                onDetail = { drinkId ->
                    bottomNavController.navigate("detail/$drinkId")
                }
            )
        }
        composable(
            "detail/{drinkId}",
            arguments = listOf(navArgument("drinkId") { type = NavType.StringType })
        ) { backStack ->
            val drinkId = backStack.arguments!!.getString("drinkId")!!
            DrinkDetailScreen(
                drinkId = drinkId,
                onViewReviews = {
                    bottomNavController.navigate("reviews/$drinkId")
                }
            )
        }
        composable(
            "reviews/{drinkId}",
            arguments = listOf(navArgument("drinkId") { type = NavType.StringType })
        ) { backStack ->
            val drinkId = backStack.arguments!!.getString("drinkId")!!
            ReviewScreen(
                drinkId = drinkId,
                onWriteReview = {
                    bottomNavController.navigate("writeReview/$drinkId")
                }
            )
        }
        composable(
            "writeReview/{drinkId}",
            arguments = listOf(navArgument("drinkId") { type = NavType.StringType })
        ) { backStack ->
            val drinkId = backStack.arguments!!.getString("drinkId")!!
            WriteReviewScreen(
                drinkId = drinkId,
                onSubmit = { /* 저장 로직 */ rootNavController.popBackStack() }
            )
        }
    }
}