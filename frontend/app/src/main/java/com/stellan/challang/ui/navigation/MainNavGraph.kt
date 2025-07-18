package com.stellan.challang.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.stellan.challang.ui.screen.archive.ArchiveScreen
import com.stellan.challang.ui.viewmodel.DrinkViewModel

fun NavGraphBuilder.mainNavGraph(rootNavController: NavHostController,
                                 bottomNavController: NavHostController,
                                 drinkViewModel: DrinkViewModel
) {
    homeNavGraph(rootNavController, bottomNavController, drinkViewModel)
    composable("archive") { ArchiveScreen() }
    myPageNavGraph(rootNavController, bottomNavController)
}
