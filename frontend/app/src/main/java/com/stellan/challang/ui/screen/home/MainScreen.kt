package com.stellan.challang.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stellan.challang.ui.navigation.mainNavGraph
import com.stellan.challang.ui.theme.PaperlogyFamily

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(bottomNavController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)

            ) {
                NavHost(
                    navController  = bottomNavController,
                    startDestination = "home"
                ) {
                    mainNavGraph(rootNavController, bottomNavController)
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFD9D9D9)
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val tabs = listOf(
        BottomTab("curating",    Icons.Outlined.Home,            "Home"),
        BottomTab("archive", Icons.Outlined.BookmarkBorder, "Archive"),
        BottomTab("mypageMain",  Icons.Default.Menu,            "My Page")
    )

    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value?.destination?.route

    NavigationBar(containerColor = Color.White) {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected   = currentRoute == tab.route,
                onClick    = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                icon       = { Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.label,
                    modifier = Modifier.size(36.dp)
                    ) },
                label      = { Text(
                    text = tab.label,
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-6).dp)
                    ) },
                colors     = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Color.Black,
                    selectedTextColor   = Color.Black,
                    unselectedIconColor = Color(0xFFC8C8C8),
                    unselectedTextColor = Color(0xFFC8C8C8),
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}

private data class BottomTab(
    val route: String,
    val icon: ImageVector,
    val label: String
)