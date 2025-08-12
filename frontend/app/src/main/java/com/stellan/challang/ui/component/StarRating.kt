package com.stellan.challang.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    starSize: Dp = 48.dp,
    onRatingChanged: (Int) -> Unit = {}
) {
    Row(modifier = modifier) {
        for (i in 0 until 5) {
            val filled = i < rating
            Icon(
                imageVector = if (filled) Icons.Default.Star else Icons.Default.Star,
                contentDescription = null,
                tint = if (filled) Color(0xFFFFD70F) else Color(0xFFD9D9D9),
                modifier = Modifier
                    .size(starSize)
                    .clickable { onRatingChanged(i + 1) }
            )
        }
    }
}