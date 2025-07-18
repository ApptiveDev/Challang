package com.stellan.challang.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingDecimal(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Dp = 18.dp
) {
    Row(modifier = modifier) {
        for (i in 0 until 5) {
            val fraction = (rating - i).coerceIn(0f, 1f)
            Box(Modifier.size(starSize)) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFD9D9D9),
                    modifier = Modifier.matchParentSize()
                )
                if (fraction > 0f) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD70F),
                        modifier = Modifier
                            .matchParentSize()
                            .drawWithContent {
                                val clipWidth = size.width * fraction
                                clipRect(
                                    left = 0f,
                                    top = 0f,
                                    right = clipWidth,
                                    bottom = size.height
                                ) {
                                    this@drawWithContent.drawContent()
                                }
                            }
                    )
                }
            }
        }
    }
}