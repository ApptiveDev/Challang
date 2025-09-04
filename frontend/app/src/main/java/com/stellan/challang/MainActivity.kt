package com.stellan.challang

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.stellan.challang.ui.navigation.AppNavHost
import com.stellan.challang.ui.theme.ChallangTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallangTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}