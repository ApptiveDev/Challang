package com.stellan.challang.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RectangularProgress(
    fraction: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFDEEEEE),
    progressColor: Color = Color(0xFFB2DADA),
    heightDp: Dp = 20.dp
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(heightDp)
            .clip(RectangleShape)
            .background(backgroundColor)
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .background(progressColor)
        )
    }
}