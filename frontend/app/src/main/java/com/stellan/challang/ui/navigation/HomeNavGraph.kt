package com.stellan.challang.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.stellan.challang.data.api.ApiClient
import com.stellan.challang.data.repository.DrinkRepository
import com.stellan.challang.ui.screen.home.*
import com.stellan.challang.ui.viewmodel.DrinkViewModel

fun NavGraphBuilder.homeNavGraph(
    bottomNavController: NavHostController,
    drinkViewModel: DrinkViewModel
) {
    navigation(startDestination = "curating", route = "home") {
        composable("curating") {
            CuratingScreen(
                onDetail = { drink ->
                    drinkViewModel.selectDrink(drink)
                    bottomNavController.navigate("detail")
                }
            )
        }
        composable("detail") {
            val selectedDrink by drinkViewModel.selectedDrink.collectAsState()

            selectedDrink?.let { drink ->
                DrinkDetailScreen(
                    drink = drink,
                    onViewReviews = {
                        bottomNavController.navigate("reviews/${drink.id}")
                    }
                )
            }
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
                onSubmit = { bottomNavController.popBackStack() }
            )
        }
    }
}