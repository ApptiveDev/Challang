package com.stellan.challang.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.stellan.challang.ui.screen.archive.ArchiveScreen

fun NavGraphBuilder.mainNavGraph(rootNavController: NavHostController,
                                 bottomNavController: NavHostController) {
    homeNavGraph(rootNavController, bottomNavController)
    composable("archive") { ArchiveScreen() }
    myPageNavGraph(rootNavController, bottomNavController)
}
