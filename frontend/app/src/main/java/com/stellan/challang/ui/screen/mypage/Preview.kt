package com.stellan.challang.ui.screen.mypage

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import com.stellan.challang.ui.screen.auth.ProfileStepThree
import androidx. navigation. compose. rememberNavController

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyPageScreenPreview() {
    // Dummy NavController
    val dummyNavController = rememberNavController()

    MyPageScreen(
        rootNavController = dummyNavController,
        onHelpClick = {},
        onLogoutClick = {},
        onWithdrawClick = {}
    )
}


